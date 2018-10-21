package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;

import org.apache.http.impl.client.CloseableHttpClient;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileDataJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileId;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.Downloader;
import com.google.gson.Gson;

public class FileModpackParseTask extends ModpackParseTask {

	/*
	 * Tools
	 */

	protected Gson gson;
	protected CloseableHttpClient client;
	protected DocumentBuilder docBuilder;

	public FileModpackParseTask(Gson gson, CloseableHttpClient client,
			DocumentBuilder docBuilder, Path fromPath) {
		this.gson = gson;
		this.client = client;
		this.docBuilder = docBuilder;

		updateFromPath(fromPath);
		updateModpackPath(fromPath);
	}

	@Override
	protected ModpackParseResult call() throws Exception {
		FileId id = ModpackXmlParser.parseModpackBin(docBuilder, getFromPath());
		if (id != null) {
			// is this modpack file an xml?
			updateMessage("Loaded xml modpack, downloading zip...");
			FileDataJson data = AddonUtils.getAddonFile(client, gson, id);

			FileJson file =
					new FileJson.Builder(id.getProjectID(), id.getFileID())
							.setFileData(data).build();
			updateProject(file);

			Path modpackPath = Files.createTempFile("modpack", ".zip");
			modpackPath.toFile().deleteOnExit();
			Downloader downloader =
					new Downloader(client, data.getDownloadUrl(), modpackPath);
			downloader.progressProperty().addListener((o, oldVal,
					newVal) -> updateProgress(newVal.doubleValue() * 0.9d, 1d));
			downloader.messageProperty()
					.addListener((o, oldVal, newVal) -> updateMessage(newVal));
			downloader.call();
			updateModpackPath(modpackPath);
		}

		updateMessage("Parsing modpack...");
		Modpack modpack = new Modpack(getModpackPath());
		modpack.parseBaseManifest(gson);

		updateModpack(modpack);
		updateMessage("Done.");
		updateProgress(1d, 1d);

		return buildResult();
	}

}
