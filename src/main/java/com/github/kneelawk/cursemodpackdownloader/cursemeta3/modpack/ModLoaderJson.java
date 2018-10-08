/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModLoaderJson {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("primary")
	@Expose
	private Boolean primary;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getPrimary() {
		return primary;
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}

}
