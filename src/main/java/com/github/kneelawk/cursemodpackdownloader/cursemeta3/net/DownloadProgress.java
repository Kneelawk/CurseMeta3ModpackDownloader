package com.github.kneelawk.cursemodpackdownloader.cursemeta3.net;

public class DownloadProgress {
	private long currentProgress;
	private long contentLength;

	public DownloadProgress(long currentProgress, long contentLength) {
		super();
		this.currentProgress = currentProgress;
		this.contentLength = contentLength;
	}

	public long getCurrentProgress() {
		return currentProgress;
	}

	public long getContentLength() {
		return contentLength;
	}
}
