/*
 * Created with help from http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileJson implements FileId {

	@SerializedName("projectID")
	@Expose
	private long projectID;
	@SerializedName("fileID")
	@Expose
	private long fileID;
	@SerializedName("required")
	@Expose
	private boolean required = true;
	@SerializedName("fileData")
	@Expose
	private FileDataJson fileData;
	@SerializedName("fileError")
	@Expose
	private boolean fileError;

	public FileJson() {
		super();
	}

	public FileJson(long projectID, long fileID) {
		super();
		this.projectID = projectID;
		this.fileID = fileID;
	}

	public FileJson(long projectID, long fileID, boolean required,
			FileDataJson fileData, boolean fileError) {
		super();
		this.projectID = projectID;
		this.fileID = fileID;
		this.required = required;
		this.fileData = fileData;
		this.fileError = fileError;
	}

	public long getProjectID() {
		return projectID;
	}

	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

	public long getFileID() {
		return fileID;
	}

	public void setFileID(long fileID) {
		this.fileID = fileID;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public FileDataJson getFileData() {
		return fileData;
	}

	public void setFileData(FileDataJson fileData) {
		this.fileData = fileData;
	}

	public boolean isFileError() {
		return fileError;
	}

	public void setFileError(boolean fileError) {
		this.fileError = fileError;
	}

}
