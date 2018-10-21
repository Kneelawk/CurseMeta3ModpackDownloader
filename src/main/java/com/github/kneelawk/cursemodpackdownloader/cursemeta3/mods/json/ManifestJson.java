/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ManifestJson {

	@SerializedName("minecraft")
	@Expose
	private MinecraftJson minecraft;
	@SerializedName("manifestType")
	@Expose
	private String manifestType;
	@SerializedName("manifestVersion")
	@Expose
	private long manifestVersion;
	@SerializedName("name")
	@Expose
	private String name;
	@SerializedName("version")
	@Expose
	private String version;
	@SerializedName("author")
	@Expose
	private String author;
	@SerializedName("projectID")
	@Expose
	private long projectID;
	@SerializedName("files")
	@Expose
	private List<FileJson> files = null;
	@SerializedName("overrides")
	@Expose
	private String overrides;

	public MinecraftJson getMinecraft() {
		return minecraft;
	}

	public void setMinecraft(MinecraftJson minecraft) {
		this.minecraft = minecraft;
	}

	public String getManifestType() {
		return manifestType;
	}

	public void setManifestType(String manifestType) {
		this.manifestType = manifestType;
	}

	public long getManifestVersion() {
		return manifestVersion;
	}

	public void setManifestVersion(long manifestVersion) {
		this.manifestVersion = manifestVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getProjectID() {
		return projectID;
	}

	public void setProjectID(long projectID) {
		this.projectID = projectID;
	}

	public List<FileJson> getFiles() {
		return files;
	}

	public void setFiles(List<FileJson> files) {
		this.files = files;
	}

	public String getOverrides() {
		return overrides;
	}

	public void setOverrides(String overrides) {
		this.overrides = overrides;
	}

}
