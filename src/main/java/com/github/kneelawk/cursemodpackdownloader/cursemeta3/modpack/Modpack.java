package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Modpack {
	public static final String MANIFEST_LOOKUP_URL =
			"https://staging_cursemeta.dries007.net/api/v3/manifest";

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

	public void parseQueriedManifest(CloseableHttpClient client, Gson gson)
			throws IOException {
		HttpPost request = new HttpPost(MANIFEST_LOOKUP_URL);
		InputStreamEntity entity = new InputStreamEntity(getManifestStream(),
				ContentType.APPLICATION_OCTET_STREAM);
		entity.setChunked(true);
		request.setEntity(entity);
		try (CloseableHttpResponse response = client.execute(request)) {
			manifest =
					gson.fromJson(EntityUtils.toString(entity), ManifestJson.class);
		}
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
					Files.copy(from, to);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
