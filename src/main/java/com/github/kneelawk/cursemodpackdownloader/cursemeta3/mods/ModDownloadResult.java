package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.DownloadProgress;

import java.nio.file.Path;

public class ModDownloadResult {
    private FileJson file;
    private Path downloadTo;
    private DownloadProgress progress;

    public ModDownloadResult(FileJson file, Path downloadTo,
                             DownloadProgress progress) {
        super();
        this.file = file;
        this.downloadTo = downloadTo;
        this.progress = progress;
    }

    public FileJson getFile() {
        return file;
    }

    public Path getDownloadTo() {
        return downloadTo;
    }

    public DownloadProgress getProgress() {
        return progress;
    }
}
