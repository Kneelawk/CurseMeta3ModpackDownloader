/*
 * Created with http://www.jsonschema2pojo.org/
 */
package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModLoaderJson {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("primary")
    @Expose
    private boolean primary;

    public ModLoaderJson() {
        super();
    }

    public ModLoaderJson(String id, boolean primary) {
        super();
        this.id = id;
        this.primary = primary;
    }

    public String getId() {
        return id;
    }

    public boolean isPrimary() {
        return primary;
    }

    public static class Builder {
        private String id;
        private boolean primary;

        public Builder() {
            super();
        }

        public Builder(ModLoaderJson o) {
            super();
            this.id = o.getId();
            this.primary = o.isPrimary();
        }

        public String getId() {
            return id;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public boolean isPrimary() {
            return primary;
        }

        public Builder setPrimary(boolean primary) {
            this.primary = primary;
            return this;
        }
    }
}
