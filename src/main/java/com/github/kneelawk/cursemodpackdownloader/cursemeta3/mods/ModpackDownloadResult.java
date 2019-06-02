package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import javafx.collections.ObservableList;

import java.nio.file.Path;

public class ModpackDownloadResult {
	private Modpack modpack;
	private Path toDir;
	private ObservableList<FileJson> successfulDownloads;
	private ObservableList<FileJson> failedDownloads;

	public ModpackDownloadResult(Modpack modpack, Path toDir,
								 ObservableList<FileJson> successfulDownloads,
								 ObservableList<FileJson> failedDownloads) {
		super();
		this.modpack = modpack;
		this.toDir = toDir;
		this.successfulDownloads = successfulDownloads;
		this.failedDownloads = failedDownloads;
	}

	public Modpack getModpack() {
		return modpack;
	}

	public Path getToDir() {
		return toDir;
	}

	public ObservableList<FileJson> getSuccessfulDownloads() {
		return successfulDownloads;
	}

	public ObservableList<FileJson> getFailedDownloads() {
		return failedDownloads;
	}
}
