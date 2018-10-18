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
	private Long id;
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
	private Long releaseType;
	@SerializedName("fileStatus")
	@Expose
	private Long fileStatus;
	@SerializedName("downloadUrl")
	@Expose
	private String downloadUrl;
	@SerializedName("isAlternate")
	@Expose
	private Boolean isAlternate;
	@SerializedName("alternateFileId")
	@Expose
	private Long alternateFileId;
	@SerializedName("dependencies")
	@Expose
	private List<DependencyJson> dependencies = null;
	@SerializedName("isAvailable")
	@Expose
	private Boolean isAvailable;
	@SerializedName("modules")
	@Expose
	private List<ModuleJson> modules = null;
	@SerializedName("packageFingerprint")
	@Expose
	private Long packageFingerprint;
	@SerializedName("gameVersion")
	@Expose
	private List<String> gameVersion = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(Long releaseType) {
		this.releaseType = releaseType;
	}

	public Long getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(Long fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public Boolean getIsAlternate() {
		return isAlternate;
	}

	public void setIsAlternate(Boolean isAlternate) {
		this.isAlternate = isAlternate;
	}

	public Long getAlternateFileId() {
		return alternateFileId;
	}

	public void setAlternateFileId(Long alternateFileId) {
		this.alternateFileId = alternateFileId;
	}

	public List<DependencyJson> getDependencies() {
		return dependencies;
	}

	public void setDependencies(List<DependencyJson> dependencies) {
		this.dependencies = dependencies;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public List<ModuleJson> getModules() {
		return modules;
	}

	public void setModules(List<ModuleJson> modules) {
		this.modules = modules;
	}

	public Long getPackageFingerprint() {
		return packageFingerprint;
	}

	public void setPackageFingerprint(Long packageFingerprint) {
		this.packageFingerprint = packageFingerprint;
	}

	public List<String> getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(List<String> gameVersion) {
		this.gameVersion = gameVersion;
	}

}
