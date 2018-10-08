package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

import java.nio.file.Path;

public class DownloadResult {
	private FileDataJson fileData;
	private Path downloadTo;
	private DownloadProgress progress;

	public DownloadResult(FileDataJson fileData, Path downloadTo,
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
