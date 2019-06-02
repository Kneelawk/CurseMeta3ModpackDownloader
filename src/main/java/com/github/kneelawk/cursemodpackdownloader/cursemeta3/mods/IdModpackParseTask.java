package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileDataJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.ClientManager;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.DownloaderTask;
import com.google.gson.Gson;
import org.apache.http.impl.client.CloseableHttpClient;

import java.nio.file.Files;
import java.nio.file.Path;

public class IdModpackParseTask extends ModpackParseTask {

	/*
	 * Tools
	 */

	protected Gson gson;
	protected ClientManager manager;

	public IdModpackParseTask(Gson gson, ClientManager manager, FileJson file) {
		this.gson = gson;
		this.manager = manager;
		updateProject(file);
	}

	@Override
	protected ModpackParseResult call() throws Exception {
		CloseableHttpClient client = manager.getClient();

		FileJson file = getProject();
		if (file.getFileData() == null) {
			FileJson.Builder fjb = new FileJson.Builder(file);
			FileDataJson data = AddonUtils.getAddonFile(client, gson, file);
			fjb.setFileData(data);
			file = fjb.build();
			updateProject(file);
		}
		FileDataJson data = file.getFileData();

		updateMessage("Downloading modpack zip...");
		Path modpackPath = Files.createTempFile("modpack", ".zip");
		modpackPath.toFile().deleteOnExit();
		DownloaderTask downloader =
				new DownloaderTask(manager, data.getDownloadUrl(), modpackPath);
		downloader.progressProperty().addListener((o, oldVal,
												   newVal) -> updateProgress(newVal.doubleValue() * 0.9d, 1d));
		downloader.messageProperty()
				.addListener((o, oldVal, newVal) -> updateMessage(newVal));
		downloader.run();
		updateModpackPath(modpackPath);

		updateMessage("Parsing modpack...");
		Modpack modpack = new Modpack(modpackPath);
		modpack.parseBaseManifest(gson);

		updateModpack(modpack);
		updateMessage("Done.");
		updateProgress(1d, 1d);

		return buildResult();
	}

}
