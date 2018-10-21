package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.CloseableHttpClient;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileId;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.ManifestJson;
import com.google.gson.Gson;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ModpackDownloadTask extends Task<ModpackDownloadResult> {

	/*
	 * Tools
	 */

	protected CloseableHttpClient client;
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

	public ModpackDownloadTask(CloseableHttpClient client, Gson gson,
			Modpack modpack, Path toDir, int numThreads) {
		super();
		this.client = client;
		this.gson = gson;
		this.modpack = modpack;
		this.toDir = toDir;

		executor = Executors.newFixedThreadPool(numThreads, runnable -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		int numFiles = modpack.getManifest().getFiles().size();

		tasks = new SimpleListProperty<>(this, "tasks");
		totalDownloads =
				new SimpleIntegerProperty(this, "totalDownloads", numFiles);
		successfulDownloads =
				new SimpleListProperty<>(this, "successfulDownloads");
		failedDownloads = new SimpleListProperty<>(this, "failedDownloads");

		updateMessage("Initializing modpack download...");
	}

	protected final void setTasks(ObservableList<ModDownloadTask> l) {
		tasks.set(l);
	}

	protected final void addTask(ModDownloadTask t) {
		tasks.add(t);
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
		successfulDownloads.add(file);
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
		failedDownloads.add(file);
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

		updateMessage("Extracting overrides...");
		modpack.extractOverrides(toDir);

		Path modsDir = toDir.resolve("mods");

		CountDownLatch latch = new CountDownLatch(getTotalDownloads());

		updateMessage("Downloading mods... 0 / " + getTotalDownloads());
		for (FileJson file : manifest.getFiles()) {
			ModDownloadTask task = new ModDownloadTask(client, gson,
					manifest.getMinecraft().getVersion(), file, modsDir);
			tasks.add(task);
			task.setOnSucceeded(e -> {
				addSuccessfulDownload(task.getFile());
				updateProgress(getSuccessfulDownloads().size(),
						getTotalDownloads());
				updateMessage(String.format("Downloading mods... %s / %s",
						getSuccessfulDownloads(), getTotalDownloads()));
				latch.countDown();
			});
			task.setOnFailed(e -> {
				addFaildDownload(task.getFile());
				latch.countDown();
			});
			task.setOnCancelled(e -> {
				latch.countDown();
			});
		}
		return null;
	}

}
