package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;

import java.nio.file.Path;

public class ModpackParseResult {
    private Path fromPath;
    private Path modpackPath;
    private FileJson project;
    private Modpack modpack;

    public ModpackParseResult(Path fromPath, Path modpackPath, FileJson project,
                              Modpack modpack) {
        super();
        this.fromPath = fromPath;
        this.modpackPath = modpackPath;
        this.project = project;
        this.modpack = modpack;
    }

    public Path getFromPath() {
        return fromPath;
    }

    public Path getModpackPath() {
        return modpackPath;
    }

    public FileJson getProject() {
        return project;
    }

    public Modpack getModpack() {
        return modpack;
    }
}
