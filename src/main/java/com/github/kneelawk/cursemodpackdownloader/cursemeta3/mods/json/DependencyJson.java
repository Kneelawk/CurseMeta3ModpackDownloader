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

    public DependencyJson() {
        super();
    }

    public DependencyJson(long addonId, long type) {
        super();
        this.addonId = addonId;
        this.type = type;
    }

    public long getAddonId() {
        return addonId;
    }

    public long getType() {
        return type;
    }

    public static class Builder {
        private long addonId;
        private long type;

        public Builder() {
            super();
        }

        public Builder(DependencyJson o) {
            super();
            this.addonId = o.getAddonId();
            this.type = o.getType();
        }

        public DependencyJson build() {
            return new DependencyJson(addonId, type);
        }

        public long getAddonId() {
            return addonId;
        }

        public Builder setAddonId(long addonId) {
            this.addonId = addonId;
            return this;
        }

        public long getType() {
            return type;
        }

        public Builder setType(long type) {
            this.type = type;
            return this;
        }
    }
}
