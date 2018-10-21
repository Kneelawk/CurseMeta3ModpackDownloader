/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModLoaderJson {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("primary")
	@Expose
	private boolean primary;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

}
