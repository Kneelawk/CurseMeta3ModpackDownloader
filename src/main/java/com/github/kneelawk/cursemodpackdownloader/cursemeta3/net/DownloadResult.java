package com.github.kneelawk.cursemodpackdownloader.cursemeta3.net;

import java.nio.file.Path;

public class DownloadResult {
    private String downloadFrom;
    private Path downloadTo;
    private DownloadProgress progress;

    public DownloadResult(String downloadFrom, Path downloadTo,
                          DownloadProgress progress) {
        super();
        this.downloadFrom = downloadFrom;
        this.downloadTo = downloadTo;
        this.progress = progress;
    }

    public String getDownloadFrom() {
        return downloadFrom;
    }

    public Path getDownloadTo() {
        return downloadTo;
    }

    public DownloadProgress getProgress() {
        return progress;
    }
}
