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

	public long getFileID() {
		return fileID;
	}

	public boolean isRequired() {
		return required;
	}

	public FileDataJson getFileData() {
		return fileData;
	}

	public boolean isFileError() {
		return fileError;
	}

	public static class Builder {
		private long projectID;
		private long fileID;
		private boolean required = true;
		private FileDataJson fileData;
		private boolean fileError;

		public Builder() {
			super();
		}

		public Builder(FileJson o) {
			super();
			this.projectID = o.getProjectID();
			this.fileID = o.getFileID();
			this.required = o.isRequired();
			this.fileData = o.getFileData();
			this.fileError = o.isFileError();
		}

		public Builder(long projectID, long fileID) {
			super();
			this.projectID = projectID;
			this.fileID = fileID;
		}

		public Builder(FileId id) {
			super();
			this.projectID = id.getProjectID();
			this.fileID = id.getProjectID();
		}

		public FileJson build() {
			return new FileJson(projectID, fileID, required, fileData,
					fileError);
		}

		public long getProjectID() {
			return projectID;
		}

		public Builder setProjectID(long projectID) {
			this.projectID = projectID;
			return this;
		}

		public long getFileID() {
			return fileID;
		}

		public Builder setFileID(long fileID) {
			this.fileID = fileID;
			return this;
		}

		public boolean isRequired() {
			return required;
		}

		public Builder setRequired(boolean required) {
			this.required = required;
			return this;
		}

		public FileDataJson getFileData() {
			return fileData;
		}

		public Builder setFileData(FileDataJson fileData) {
			this.fileData = fileData;
			return this;
		}

		public boolean isFileError() {
			return fileError;
		}

		public Builder setFileError(boolean fileError) {
			this.fileError = fileError;
			return this;
		}
	}
}
