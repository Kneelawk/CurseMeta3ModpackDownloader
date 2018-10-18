/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DependencyJson {

	@SerializedName("addonId")
	@Expose
	private Long addonId;
	@SerializedName("type")
	@Expose
	private Long type;

	public Long getAddonId() {
		return addonId;
	}

	public void setAddonId(Long addonId) {
		this.addonId = addonId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

}
