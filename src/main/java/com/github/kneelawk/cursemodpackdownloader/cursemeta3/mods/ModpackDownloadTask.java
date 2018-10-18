package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.binding.NumberBinding;
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
	 * Fields
	 */

	protected Modpack modpack;
	protected Path toDir;

	/*
	 * Objects
	 */

	protected ExecutorService executor;

	/*
	 * Properties
	 */

	protected ListProperty<ModDownloadTask> tasks;
	protected ReadOnlyIntegerProperty totalDownloads;
	protected IntegerProperty successfulDownloads;
	protected IntegerProperty failedDownloads;
	protected NumberBinding finishedDownloads;

	public ModpackDownloadTask(Modpack modpack, Path toDir, int numThreads) {
		super();
		this.modpack = modpack;
		this.toDir = toDir;

		executor = Executors.newFixedThreadPool(numThreads, runnable -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});
		
		tasks = new SimpleListProperty<>(this, "tasks");
		totalDownloads = new SimpleIntegerProperty(this, "totalDownloads", modpack.getManifest().getFiles().size());
		successfulDownloads = new SimpleIntegerProperty(this, "successfulDownloads", 0);
		failedDownloads = new SimpleIntegerProperty(this, "failedDownloads", 0);
		finishedDownloads = successfulDownloads.add(failedDownloads);
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
		return null;
	}

}
