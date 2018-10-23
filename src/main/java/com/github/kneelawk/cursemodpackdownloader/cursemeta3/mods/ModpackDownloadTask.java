package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.ManifestJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.BadResponseCodeException;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.ClientManager;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ModpackDownloadTask extends Task<ModpackDownloadResult> {

	/*
	 * Tools
	 */

	protected ClientManager manager;
	protected Gson gson;
	protected ExecutorService executor;

	/*
	 * Fields
	 */

	protected Modpack modpack;
	protected Path toDir;

	/*
	 * Properties
	 */

	protected ListProperty<ModDownloadTask> tasks;
	protected ReadOnlyIntegerProperty totalDownloads;
	protected ListProperty<FileJson> successfulDownloads;
	protected ListProperty<FileJson> failedDownloads;

	public ModpackDownloadTask(ClientManager manager, Gson gson,
			Modpack modpack, Path toDir, int numThreads) {
		super();
		this.manager = manager;
		this.gson = gson;
		this.modpack = modpack;
		this.toDir = toDir;

		executor = Executors.newFixedThreadPool(numThreads, runnable -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		int numFiles = modpack.getManifest().getFiles().size();

		tasks = new SimpleListProperty<>(this, "tasks",
				FXCollections.observableArrayList());
		totalDownloads =
				new SimpleIntegerProperty(this, "totalDownloads", numFiles);
		successfulDownloads = new SimpleListProperty<>(this,
				"successfulDownloads", FXCollections.observableArrayList());
		failedDownloads = new SimpleListProperty<>(this, "failedDownloads",
				FXCollections.observableArrayList());

		updateMessage("Initializing modpack download...");
	}

	protected final void setTasks(ObservableList<ModDownloadTask> l) {
		tasks.set(l);
	}

	protected final void addTask(ModDownloadTask t) {
		if (Platform.isFxApplicationThread()) {
			tasks.add(t);
		} else {
			Platform.runLater(() -> tasks.add(t));
		}
	}

	protected final void removeTask(ModDownloadTask t) {
		if (Platform.isFxApplicationThread()) {
			tasks.remove(t);
		} else {
			Platform.runLater(() -> tasks.remove(t));
		}
	}

	protected final void addAllTasks(Collection<ModDownloadTask> c) {
		tasks.addAll(c);
	}

	protected final void clearTasks() {
		tasks.clear();
	}

	public final ObservableList<ModDownloadTask> getTasks() {
		return tasks.get();
	}

	public final ReadOnlyListProperty<ModDownloadTask> tasksProperty() {
		return tasks;
	}

	public final int getTotalDownloads() {
		return totalDownloads.get();
	}

	public final ReadOnlyIntegerProperty totalDownloadsProperty() {
		return totalDownloads;
	}

	protected final void setAllSuccessfulDownloads(Collection<FileJson> files) {
		successfulDownloads.setAll(files);
	}

	protected final void addSuccessfulDownload(FileJson file) {
		if (Platform.isFxApplicationThread()) {
			successfulDownloads.add(file);
		} else {
			Platform.runLater(() -> successfulDownloads.add(file));
		}
	}

	public final ObservableList<FileJson> getSuccessfulDownloads() {
		return successfulDownloads.get();
	}

	public final ReadOnlyListProperty<FileJson> successfulDownloadsProperty() {
		return successfulDownloads;
	}

	protected final void setAllFailedDownloads(Collection<FileJson> files) {
		failedDownloads.setAll(files);
	}

	protected final void addFaildDownload(FileJson file) {
		if (Platform.isFxApplicationThread()) {
			failedDownloads.add(file);
		} else {
			Platform.runLater(() -> failedDownloads.add(file));
		}
	}

	public final ObservableList<FileJson> getFailedDownloads() {
		return failedDownloads.get();
	}

	public final ReadOnlyListProperty<FileJson> failedDownloadsProperty() {
		return failedDownloads;
	}

	@Override
	protected ModpackDownloadResult call() throws Exception {
		updateProgress(0, getTotalDownloads());

		ManifestJson manifest = modpack.getManifest();

		if (!Files.exists(toDir)) {
			Files.createDirectories(toDir);
		}

		updateMessage("Extracting overrides...");
		modpack.extractOverrides(toDir);

		Path modsDir = toDir.resolve("mods");
		if (!Files.exists(modsDir)) {
			Files.createDirectory(modsDir);
		}

		CountDownLatch latch = new CountDownLatch(getTotalDownloads());

		updateMessage("Downloading mods... 0 / " + getTotalDownloads());
		for (FileJson file : manifest.getFiles()) {
			if (file.isRequired()) {
				startModDownload(latch, manifest, file, modsDir);
			}
		}

		latch.await();

		executor.shutdown();

		return new ModpackDownloadResult(modpack, modsDir,
				getSuccessfulDownloads(), getFailedDownloads());
	}

	private void startModDownload(CountDownLatch latch, ManifestJson manifest,
			FileJson file, Path modsDir) {
		ModDownloadTask task = new ModDownloadTask(manager, gson,
				manifest.getMinecraft().getVersion(), file, modsDir);
		addTask(task);
		task.setOnSucceeded(e -> {
			// add this download to the list of successful downloads and update
			// this tasks's progress.
			addSuccessfulDownload(task.getFile());
			updateProgress(getSuccessfulDownloads().size(),
					getTotalDownloads());
			updateMessage(String.format("Downloading mods... %s / %s",
					getSuccessfulDownloads().size(), getTotalDownloads()));
			latch.countDown();
		});
		task.setOnFailed(e -> {
			// is this an error we know we can't recover from?
			if (task.getException() instanceof BadResponseCodeException) {
				// Yes, then add this file to the list of failed downloads.
				addFaildDownload(task.getFile());
				latch.countDown();
			} else {
				// No, then retry the download.
				task.getException().printStackTrace();
				removeTask(task);
				startModDownload(latch, manifest, file, modsDir);
			}
		});
		task.setOnCancelled(e -> {
			latch.countDown();
		});
		executor.execute(task);
	}
}
