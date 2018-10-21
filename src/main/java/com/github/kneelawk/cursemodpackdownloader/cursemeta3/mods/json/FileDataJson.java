/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileDataJson {

	@SerializedName("id")
	@Expose
	private long id;
	@SerializedName("fileName")
	@Expose
	private String fileName;
	@SerializedName("fileNameOnDisk")
	@Expose
	private String fileNameOnDisk;
	@SerializedName("fileDate")
	@Expose
	private String fileDate;
	@SerializedName("releaseType")
	@Expose
	private long releaseType;
	@SerializedName("fileStatus")
	@Expose
	private long fileStatus;
	@SerializedName("downloadUrl")
	@Expose
	private String downloadUrl;
	@SerializedName("isAlternate")
	@Expose
	private boolean isAlternate;
	@SerializedName("alternateFileId")
	@Expose
	private long alternateFileId;
	@SerializedName("dependencies")
	@Expose
	private List<DependencyJson> dependencies = null;
	@SerializedName("isAvailable")
	@Expose
	private boolean isAvailable;
	@SerializedName("modules")
	@Expose
	private List<ModuleJson> modules = null;
	@SerializedName("packageFingerprint")
	@Expose
	private long packageFingerprint;
	@SerializedName("gameVersion")
	@Expose
	private List<String> gameVersion = null;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileNameOnDisk() {
		return fileNameOnDisk;
	}

	public void setFileNameOnDisk(String fileNameOnDisk) {
		this.fileNameOnDisk = fileNameOnDisk;
	}

	public String getFileDate() {
		return fileDate;
	}

	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}

	public long getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(long releaseType) {
		this.releaseType = releaseType;
	}

	public long getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(long fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public boolean isAlternate() {
		return isAlternate;
	}

	public void setIsAlternate(boolean isAlternate) {
		this.isAlternate = isAlternate;
	}

	public long getAlternateFileId() {
		return alternateFileId;
	}

	public void setAlternateFileId(long alternateFileId) {
		this.alternateFileId = alternateFileId;
	}

	public List<DependencyJson> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<DependencyJson> dependencies) {
		this.dependencies = dependencies;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public List<ModuleJson> getModules() {
		return modules;
	}

	public void setModules(List<ModuleJson> modules) {
		this.modules = modules;
	}

	public long getPackageFingerprint() {
		return packageFingerprint;
	}

	public void setPackageFingerprint(long packageFingerprint) {
		this.packageFingerprint = packageFingerprint;
	}

	public List<String> getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(List<String> gameVersion) {
		this.gameVersion = gameVersion;
	}

}
