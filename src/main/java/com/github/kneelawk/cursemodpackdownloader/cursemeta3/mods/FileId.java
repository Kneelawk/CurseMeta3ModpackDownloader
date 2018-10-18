package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

public class FileId {
	private int projectId;
	private int fileId;

	public FileId(int projectId, int fileId) {
		super();
		this.projectId = projectId;
		this.fileId = fileId;
	}

	public int getProjectId() {
		return projectId;
	}

	public int getFileId() {
		return fileId;
	}
}
