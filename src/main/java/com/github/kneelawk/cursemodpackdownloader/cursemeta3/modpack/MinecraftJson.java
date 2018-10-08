/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MinecraftJson {

	@SerializedName("version")
	@Expose
	private String version;
	@SerializedName("modLoaders")
	@Expose
	private List<ModLoaderJson> modLoaders = null;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<ModLoaderJson> getModLoaders() {
		return modLoaders;
	}

	public void setModLoaders(List<ModLoaderJson> modLoaders) {
		this.modLoaders = modLoaders;
	}

}
