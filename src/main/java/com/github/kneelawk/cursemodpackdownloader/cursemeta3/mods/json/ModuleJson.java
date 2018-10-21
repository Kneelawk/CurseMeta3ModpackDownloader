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

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public long getFimgerprint() {
		return fimgerprint;
	}

	public void setFimgerprint(long fimgerprint) {
		this.fimgerprint = fimgerprint;
	}

}
