/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DependencyJson {

	@SerializedName("addonId")
	@Expose
	private long addonId;
	@SerializedName("type")
	@Expose
	private long type;

	public long getAddonId() {
		return addonId;
	}

	public void setAddonId(long addonId) {
		this.addonId = addonId;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

}
