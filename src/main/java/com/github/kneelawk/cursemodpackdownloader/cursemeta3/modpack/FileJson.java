/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

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

}
