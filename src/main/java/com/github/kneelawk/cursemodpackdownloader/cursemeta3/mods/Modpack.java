package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.ManifestJson;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class Modpack {

	private FileSystem packfs;
	private ManifestJson manifest;

	public Modpack(Path modpack) throws IOException {
		packfs = FileSystems.newFileSystem(modpack, null);
	}

	public Path getManifestPath() {
		return packfs.getPath("/manifest.json");
	}

	public InputStream getManifestStream() throws IOException {
		return Files.newInputStream(getManifestPath());
	}

	public BufferedReader getManifestReader() throws IOException {
		return Files.newBufferedReader(getManifestPath());
	}

	public void parseBaseManifest(Gson gson)
			throws JsonSyntaxException, JsonIOException, IOException {
		manifest = gson.fromJson(getManifestReader(), ManifestJson.class);
	}

	public ManifestJson getManifest() {
		return manifest;
	}

	public Path getOverridesPath() {
		if (manifest == null) {
			throw new IllegalStateException(
					"The manifest has not yet been parsed");
		}

		String overrides = manifest.getOverrides();
		if (!overrides.startsWith("/")) {
			overrides = "/" + overrides;
		}

		return packfs.getPath(overrides);
	}

	public void extractOverrides(Path toDir) throws IOException {
		Path overrides = getOverridesPath();
		Files.walk(overrides).forEach(from -> {
			Path to = toDir.resolve(overrides.relativize(from).toString());
			if (Files.isDirectory(from)) {
				try {
					Files.createDirectories(to);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Files.createDirectories(to.getParent());
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
