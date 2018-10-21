package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileDataJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.BadResponseCodeException;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.CurseURIUtils;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.DownloadProgress;
import com.google.gson.Gson;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public class ModDownloadTask extends Task<ModDownloadResult> {

	/*
	 * Tools
	 */

	protected CloseableHttpClient client;
	protected Gson gson;

	/*
	 * Fields
	 */

	protected String minecraftVersion;
	protected Path toDir;
	protected Path to;

	/*
	 * Properties
	 */

	protected ObjectProperty<FileJson> file;

	/*
	 * Internal
	 */

	protected long currentProgress;
	protected long contentLength;

	public ModDownloadTask(CloseableHttpClient client, Gson gson,
			String minecraftVersion, FileJson file, Path toDir) {
		this.client = client;
		this.gson = gson;
		this.minecraftVersion = minecraftVersion;
		this.file = new SimpleObjectProperty<>(this, "file", file);
		this.toDir = toDir;

		updateMessage("Waiting " + file.getProjectID() + "/" + file.getFileID()
				+ "...");
		updateProgress(-1, -1);
	}

	public final FileJson getFile() {
		return file.get();
	}

	public final ObjectProperty<FileJson> fileProperty() {
		return file;
	}

	@Override
	public ModDownloadResult call() throws Exception {
		updateMessage("Downloading " + this.file.get().getProjectID() + "/"
				+ this.file.get().getFileID() + "...");
		updateProgress(-1, -1);

		FileJson file = this.file.get();
		if (file.getFileData() == null) {
			// download file details if needed
			file = AddonUtils.getAddonFileOrLatest(client, gson,
					minecraftVersion, this.file.get());
			this.file.set(file);
		}
		FileDataJson data = file.getFileData();

		updateMessage("Downloading " + data.getFileNameOnDisk() + "... 0%");

		String unescapedUrl = data.getDownloadUrl();

		to = toDir.resolve(
				unescapedUrl.substring(unescapedUrl.lastIndexOf('/') + 1));

		URI saneUri =
				CurseURIUtils.sanitizeCurseDownloadUri(unescapedUrl, true);

		HttpGet request = new HttpGet(saneUri);
		try (CloseableHttpResponse response = client.execute(request)) {
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() / 100 != 2) {
				System.out.println("Status error getting file:\n\tInsane:\t"
						+ unescapedUrl + "\n\tSane:\t" + saneUri);
				throw new BadResponseCodeException(
						"Bad response status: " + status.toString());
			}

			HttpEntity entity = response.getEntity();
			contentLength = entity.getContentLength();
			InputStream is = entity.getContent();
			OutputStream os = Files.newOutputStream(to);

			updateMessage("Downloading " + data.getFileNameOnDisk() + "... 0%");
			updateProgress(0, contentLength);

			currentProgress = 0;
			byte[] buf = new byte[8192];
			int len;
			while ((len = is.read(buf)) >= 0) {
				os.write(buf, 0, len);

				currentProgress += len;
				updateMessage(String.format("Downloading %s... %.1f%%",
						data.getFileNameOnDisk(), ((double) currentProgress)
								/ ((double) contentLength) * 100));
				updateProgress(currentProgress, contentLength);

				if (isCancelled()) {
					EntityUtils.consume(entity);
					return new ModDownloadResult(data, to, new DownloadProgress(
							currentProgress, contentLength));
				}
			}
		}

		return new ModDownloadResult(data, to,
				new DownloadProgress(currentProgress, contentLength));
	}
}
