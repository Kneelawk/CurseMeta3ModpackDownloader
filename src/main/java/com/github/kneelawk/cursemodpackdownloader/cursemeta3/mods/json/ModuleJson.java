/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModuleJson {

	@SerializedName("folderName")
	@Expose
	private String folderName;
	@SerializedName("fimgerprint")
	@Expose
	private long fimgerprint;

	public ModuleJson() {
		super();
	}

	public ModuleJson(String folderName, long fimgerprint) {
		super();
		this.folderName = folderName;
		this.fimgerprint = fimgerprint;
	}

	public String getFolderName() {
		return folderName;
	}

	public long getFimgerprint() {
		return fimgerprint;
	}

	public static class Builder {
		private String folderName;
		private long fimgerprint;

		public Builder() {
			super();
		}

		public Builder(ModuleJson o) {
			super();
			this.folderName = o.getFolderName();
			this.fimgerprint = o.getFimgerprint();
		}

		public String getFolderName() {
			return folderName;
		}

		public Builder setFolderName(String folderName) {
			this.folderName = folderName;
			return this;
		}

		public long getFimgerprint() {
			return fimgerprint;
		}

		public Builder setFimgerprint(long fimgerprint) {
			this.fimgerprint = fimgerprint;
			return this;
		}
	}
}
