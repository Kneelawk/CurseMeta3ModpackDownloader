/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    private List<FileJson> files = Lists.newArrayList();
    @SerializedName("overrides")
    @Expose
    private String overrides;

    public ManifestJson() {
        super();
    }

    public ManifestJson(MinecraftJson minecraft, String manifestType,
                        long manifestVersion, String name, String version, String author,
                        long projectID, List<FileJson> files, String overrides) {
        super();
        this.minecraft = minecraft;
        this.manifestType = manifestType;
        this.manifestVersion = manifestVersion;
        this.name = name;
        this.version = version;
        this.author = author;
        this.projectID = projectID;
        this.files = files;
        this.overrides = overrides;
    }

    public MinecraftJson getMinecraft() {
        return minecraft;
    }

    public String getManifestType() {
        return manifestType;
    }

    public long getManifestVersion() {
        return manifestVersion;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public long getProjectID() {
        return projectID;
    }

    public List<FileJson> getFiles() {
        return files;
    }

    public String getOverrides() {
        return overrides;
    }

    public static class Builder {
        private MinecraftJson minecraft;
        private String manifestType;
        private long manifestVersion;
        private String name;
        private String version;
        private String author;
        private long projectID;
        private List<FileJson> files = Lists.newArrayList();
        private String overrides;

        public Builder() {
            super();
        }

        public Builder(ManifestJson o) {
            super();
            this.minecraft = o.getMinecraft();
            this.manifestType = o.getManifestType();
            this.manifestVersion = o.getManifestVersion();
            this.name = o.getName();
            this.version = o.getVersion();
            this.author = o.getAuthor();
            this.projectID = o.getProjectID();
            this.files = o.getFiles();
            this.overrides = o.getOverrides();
        }

        public ManifestJson build() {
            return new ManifestJson(minecraft, manifestType, manifestVersion,
                    name, version, author, projectID, files, overrides);
        }

        public MinecraftJson getMinecraft() {
            return minecraft;
        }

        public Builder setMinecraft(MinecraftJson minecraft) {
            this.minecraft = minecraft;
            return this;
        }

        public String getManifestType() {
            return manifestType;
        }

        public Builder setManifestType(String manifestType) {
            this.manifestType = manifestType;
            return this;
        }

        public long getManifestVersion() {
            return manifestVersion;
        }

        public Builder setManifestVersion(long manifestVersion) {
            this.manifestVersion = manifestVersion;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getVersion() {
            return version;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public String getAuthor() {
            return author;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public long getProjectID() {
            return projectID;
        }

        public Builder setProjectID(long projectID) {
            this.projectID = projectID;
            return this;
        }

        public List<FileJson> getFiles() {
            return files;
        }

        public Builder setFiles(List<FileJson> files) {
            this.files = files;
            return this;
        }

        public String getOverrides() {
            return overrides;
        }

        public Builder setOverrides(String overrides) {
            this.overrides = overrides;
            return this;
        }
    }
}
