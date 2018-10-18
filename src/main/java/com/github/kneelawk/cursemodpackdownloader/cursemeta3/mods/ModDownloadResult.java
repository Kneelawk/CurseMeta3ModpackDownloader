package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Path;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileDataJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.DownloadProgress;

public class ModDownloadResult {
	private FileDataJson fileData;
	private Path downloadTo;
	private DownloadProgress progress;

	public ModDownloadResult(FileDataJson fileData, Path downloadTo,
			DownloadProgress progress) {
		super();
		this.fileData = fileData;
		this.downloadTo = downloadTo;
		this.progress = progress;
	}

	public FileDataJson getFileData() {
		return fileData;
	}

	public Path getDownloadTo() {
		return downloadTo;
	}

	public DownloadProgress getProgress() {
		return progress;
	}
}
