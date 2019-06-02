/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MinecraftJson {

	@SerializedName("version")
	@Expose
	private String version;
	@SerializedName("modLoaders")
	@Expose
	private List<ModLoaderJson> modLoaders = Lists.newArrayList();

	public MinecraftJson() {
		super();
	}

	public MinecraftJson(String version, List<ModLoaderJson> modLoaders) {
		super();
		this.version = version;
		this.modLoaders = modLoaders;
	}

	public String getVersion() {
		return version;
	}

	public List<ModLoaderJson> getModLoaders() {
		return modLoaders;
	}

	public static class Builder {
		private String version;
		private List<ModLoaderJson> modLoaders = Lists.newArrayList();

		public Builder() {
			super();
		}

		public Builder(MinecraftJson o) {
			super();
			this.version = o.getVersion();
			this.modLoaders = o.getModLoaders();
		}

		public MinecraftJson build() {
			return new MinecraftJson(version, modLoaders);
		}

		public String getVersion() {
			return version;
		}

		public Builder setVersion(String version) {
			this.version = version;
			return this;
		}

		public List<ModLoaderJson> getModLoaders() {
			return modLoaders;
		}

		public Builder setModLoaders(List<ModLoaderJson> modLoaders) {
			this.modLoaders = modLoaders;
			return this;
		}
	}
}
