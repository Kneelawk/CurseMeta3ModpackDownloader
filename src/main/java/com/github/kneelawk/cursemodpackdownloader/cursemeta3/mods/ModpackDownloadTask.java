package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.CloseableHttpClient;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.ManifestJson;
import com.google.gson.Gson;

import javafx.beans.property.IntegerProperty;
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
	protected IntegerProperty successfulDownloads;
	protected IntegerProperty failedDownloads;

	public ModpackDownloadTask(CloseableHttpClient client, Gson gson, Modpack modpack, Path toDir, int numThreads) {
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
		totalDownloads = new SimpleIntegerProperty(this, "totalDownloads", numFiles);
		successfulDownloads = new SimpleIntegerProperty(this, "successfulDownloads", 0);
		failedDownloads = new SimpleIntegerProperty(this, "failedDownloads", 0);

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

	protected final void setSuccessfulDownloads(int i) {
		successfulDownloads.set(i);
	}

	protected final void incrementSuccessfulDownloads() {
		successfulDownloads.add(1);
	}

	public final int getSuccessfulDownloads() {
		return successfulDownloads.get();
	}

	public final ReadOnlyIntegerProperty successfulDownloadsProperty() {
		return successfulDownloads;
	}

	protected final void setFailedDownloads(int i) {
		failedDownloads.set(i);
	}

	protected final void incrementFaildDownloads() {
		failedDownloads.add(1);
	}

	public final int getFailedDownloads() {
		return failedDownloads.get();
	}

	public final ReadOnlyIntegerProperty failedDownloadsProperty() {
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
			ModDownloadTask task = new ModDownloadTask(client, gson, manifest.getMinecraft().getVersion(), file,
					modsDir);
			tasks.add(task);
			task.setOnSucceeded(e -> {
				incrementSuccessfulDownloads();
				updateProgress(getSuccessfulDownloads(), getTotalDownloads());
				updateMessage(
						String.format("Downloading mods... %s / %s", getSuccessfulDownloads(), getTotalDownloads()));
				latch.countDown();
			});
			task.setOnFailed(e -> {
				incrementFaildDownloads();
				latch.countDown();
			});
			task.setOnCancelled(e -> {
				latch.countDown();
			});
		}
		return null;
	}

}
