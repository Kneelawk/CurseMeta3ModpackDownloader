package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public abstract class ModpackParseTask extends Task<ModpackParseResult> {

	/*
	 * Properties
	 */

	protected ObjectProperty<Path> fromPath =
			new SimpleObjectProperty<>(this, "fromPath");
	protected AtomicReference<Path> fromPathUpdate = new AtomicReference<>();
	protected ObjectProperty<Path> modpackPath =
			new SimpleObjectProperty<>(this, "modpackPath");
	protected AtomicReference<Path> modpackPathUpdate = new AtomicReference<>();
	protected ObjectProperty<FileJson> project =
			new SimpleObjectProperty<>(this, "project");
	protected AtomicReference<FileJson> projectUpdate = new AtomicReference<>();
	protected ObjectProperty<Modpack> modpack =
			new SimpleObjectProperty<>(this, "modpack");
	protected AtomicReference<Modpack> modpackUpdate = new AtomicReference<>();

	protected final void setFromPath(Path fromPath) {
		this.fromPath.set(fromPath);
	}

	protected final void updateFromPath(Path fromPath) {
		if (Platform.isFxApplicationThread()) {
			setFromPath(fromPath);
		} else if (fromPathUpdate.getAndSet(fromPath) == null) {
			Platform.runLater(
					() -> setFromPath(fromPathUpdate.getAndSet(null)));
		}
	}

	public final Path getFromPath() {
		return fromPath.get();
	}

	public final ReadOnlyObjectProperty<Path> fromPathProperty() {
		return fromPath;
	}

	protected final void setModpackPath(Path modpackPath) {
		this.modpackPath.set(modpackPath);
	}

	protected final void updateModpackPath(Path modpackPath) {
		if (Platform.isFxApplicationThread()) {
			setModpackPath(modpackPath);
		} else if (modpackPathUpdate.getAndSet(modpackPath) == null) {
			Platform.runLater(
					() -> setModpackPath(modpackPathUpdate.getAndSet(null)));
		}
	}

	public final Path getModpackPath() {
		return modpackPath.get();
	}

	public final ReadOnlyObjectProperty<Path> modpackPathProperty() {
		return modpackPath;
	}

	protected final void setProject(FileJson project) {
		this.project.set(project);
	}

	protected final void updateProject(FileJson project) {
		if (Platform.isFxApplicationThread()) {
			setProject(project);
		} else if (projectUpdate.getAndSet(project) == null) {
			Platform.runLater(() -> setProject(projectUpdate.getAndSet(null)));
		}
	}

	public final FileJson getProject() {
		return project.get();
	}

	public final ReadOnlyObjectProperty<FileJson> projectProperty() {
		return project;
	}

	protected final void setModpack(Modpack modpack) {
		this.modpack.set(modpack);
	}

	protected final void updateModpack(Modpack modpack) {
		if (Platform.isFxApplicationThread()) {
			setModpack(modpack);
		} else if (modpackUpdate.getAndSet(modpack) == null) {
			Platform.runLater(() -> setModpack(modpackUpdate.getAndSet(null)));
		}
	}

	public final Modpack getModpack() {
		return modpack.get();
	}

	public final ReadOnlyObjectProperty<Modpack> modpackProperty() {
		return modpack;
	}

	protected final void runInFxThread(Runnable r) {
		if (Platform.isFxApplicationThread()) {
			r.run();
		} else {
			Platform.runLater(r);
		}
	}

	protected final ModpackParseResult buildResult() {
		return new ModpackParseResult(getFromPath(), getModpackPath(),
				getProject(), getModpack());
	}
}
