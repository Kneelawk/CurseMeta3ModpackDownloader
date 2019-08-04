/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileDataJson {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("fileName")
    @Expose
    private String fileName;
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
    private List<DependencyJson> dependencies = Lists.newArrayList();
    @SerializedName("isAvailable")
    @Expose
    private boolean isAvailable;
    @SerializedName("modules")
    @Expose
    private List<ModuleJson> modules = Lists.newArrayList();
    @SerializedName("packageFingerprint")
    @Expose
    private long packageFingerprint;
    @SerializedName("gameVersion")
    @Expose
    private List<String> gameVersion = Lists.newArrayList();

    public FileDataJson() {
        super();
    }

    public FileDataJson(long id, String displayName, String fileName,
                        String fileDate, long releaseType, long fileStatus,
                        String downloadUrl, boolean isAlternate, long alternateFileId,
                        List<DependencyJson> dependencies, boolean isAvailable,
                        List<ModuleJson> modules, long packageFingerprint,
                        List<String> gameVersion) {
        super();
        this.id = id;
        this.displayName = displayName;
        this.fileName = fileName;
        this.fileDate = fileDate;
        this.releaseType = releaseType;
        this.fileStatus = fileStatus;
        this.downloadUrl = downloadUrl;
        this.isAlternate = isAlternate;
        this.alternateFileId = alternateFileId;
        this.dependencies = dependencies;
        this.isAvailable = isAvailable;
        this.modules = modules;
        this.packageFingerprint = packageFingerprint;
        this.gameVersion = gameVersion;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDate() {
        return fileDate;
    }

    public long getReleaseType() {
        return releaseType;
    }

    public long getFileStatus() {
        return fileStatus;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public boolean isAlternate() {
        return isAlternate;
    }

    public long getAlternateFileId() {
        return alternateFileId;
    }

    public List<DependencyJson> getDependencies() {
        return dependencies;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public List<ModuleJson> getModules() {
        return modules;
    }

    public long getPackageFingerprint() {
        return packageFingerprint;
    }

    public List<String> getGameVersion() {
        return gameVersion;
    }

    public static class Builder {
        private long id;
        private String fileName;
        private String fileNameOnDisk;
        private String fileDate;
        private long releaseType;
        private long fileStatus;
        private String downloadUrl;
        private boolean isAlternate;
        private long alternateFileId;
        private List<DependencyJson> dependencies = Lists.newArrayList();
        private boolean isAvailable;
        private List<ModuleJson> modules = Lists.newArrayList();
        private long packageFingerprint;
        private List<String> gameVersion = Lists.newArrayList();

        public Builder() {
            super();
        }

        public Builder(FileDataJson o) {
            super();
            this.id = o.getId();
            this.fileName = o.getDisplayName();
            this.fileNameOnDisk = o.getFileName();
            this.fileDate = o.getFileDate();
            this.releaseType = o.getReleaseType();
            this.fileStatus = o.getFileStatus();
            this.downloadUrl = o.getDownloadUrl();
            this.isAlternate = o.isAlternate();
            this.alternateFileId = o.getAlternateFileId();
            this.dependencies = o.getDependencies();
            this.isAvailable = o.isAvailable();
            this.modules = o.getModules();
            this.packageFingerprint = o.getPackageFingerprint();
            this.gameVersion = o.getGameVersion();
        }

        public FileDataJson build() {
            return new FileDataJson(id, fileName, fileNameOnDisk, fileDate,
                    releaseType, fileStatus, downloadUrl, isAlternate,
                    alternateFileId, dependencies, isAvailable, modules,
                    packageFingerprint, gameVersion);
        }

        public long getId() {
            return id;
        }

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public String getFileName() {
            return fileName;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public String getFileNameOnDisk() {
            return fileNameOnDisk;
        }

        public Builder setFileNameOnDisk(String fileNameOnDisk) {
            this.fileNameOnDisk = fileNameOnDisk;
            return this;
        }

        public String getFileDate() {
            return fileDate;
        }

        public Builder setFileDate(String fileDate) {
            this.fileDate = fileDate;
            return this;
        }

        public long getReleaseType() {
            return releaseType;
        }

        public Builder setReleaseType(long releaseType) {
            this.releaseType = releaseType;
            return this;
        }

        public long getFileStatus() {
            return fileStatus;
        }

        public Builder setFileStatus(long fileStatus) {
            this.fileStatus = fileStatus;
            return this;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public Builder setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public boolean isAlternate() {
            return isAlternate;
        }

        public Builder setAlternate(boolean isAlternate) {
            this.isAlternate = isAlternate;
            return this;
        }

        public long getAlternateFileId() {
            return alternateFileId;
        }

        public Builder setAlternateFileId(long alternateFileId) {
            this.alternateFileId = alternateFileId;
            return this;
        }

        public List<DependencyJson> getDependencies() {
            return dependencies;
        }

        public Builder setDependencies(List<DependencyJson> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public Builder setAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public List<ModuleJson> getModules() {
            return modules;
        }

        public Builder setModules(List<ModuleJson> modules) {
            this.modules = modules;
            return this;
        }

        public long getPackageFingerprint() {
            return packageFingerprint;
        }

        public Builder setPackageFingerprint(long packageFingerprint) {
            this.packageFingerprint = packageFingerprint;
            return this;
        }

        public List<String> getGameVersion() {
            return gameVersion;
        }

        public Builder setGameVersion(List<String> gameVersion) {
            this.gameVersion = gameVersion;
            return this;
        }
    }
}
