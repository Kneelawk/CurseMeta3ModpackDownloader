/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileJson {

	@SerializedName("projectID")
	@Expose
	private Long projectID;
	@SerializedName("fileID")
	@Expose
	private Long fileID;
	@SerializedName("required")
	@Expose
	private Boolean required = true;
	@SerializedName("fileData")
	@Expose
	private FileDataJson fileData;
	@SerializedName("fileError")
	@Expose
	private Boolean fileError;

	public Long getProjectID() {
		return projectID;
	}

	public void setProjectID(Long projectID) {
		this.projectID = projectID;
	}

	public Long getFileID() {
		return fileID;
	}

	public void setFileID(Long fileID) {
		this.fileID = fileID;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public FileDataJson getFileData() {
		return fileData;
	}

	public void setFileData(FileDataJson fileData) {
		this.fileData = fileData;
	}

	public Boolean getFileError() {
		return fileError;
	}

	public void setFileError(Boolean fileError) {
		this.fileError = fileError;
	}

}
