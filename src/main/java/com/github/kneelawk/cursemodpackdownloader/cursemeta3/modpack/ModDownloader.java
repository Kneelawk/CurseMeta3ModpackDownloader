package com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javafx.concurrent.Task;

public class ModDownloader extends Task<DownloadResult> {
	private CloseableHttpClient client;
	private FileDataJson data;
	private Path to;

	private long currentProgress;
	private long contentLength;

	public ModDownloader(CloseableHttpClient client, FileDataJson data,
			Path to) {
		this.client = client;
		this.data = data;
		this.to = to;
	}

	@Override
	public DownloadResult call() {
		HttpGet request = new HttpGet(data.getDownloadUrl());
		try (CloseableHttpResponse response = client.execute(request)) {
			HttpEntity entity = response.getEntity();
			contentLength = entity.getContentLength();
			InputStream is = entity.getContent();
			OutputStream os = Files.newOutputStream(to);

			updateMessage("Downloading... 0%");
			updateProgress(0, contentLength);

			currentProgress = 0;
			byte[] buf = new byte[8192];
			int len;
			while ((len = is.read(buf)) >= 0) {
				os.write(buf, 0, len);

				currentProgress += len;
				updateMessage(String.format("Downloading... %.1f%%",
						((double) currentProgress) / ((double) contentLength)
								* 100));
				updateProgress(currentProgress, contentLength);

				if (isCancelled()) {
					EntityUtils.consume(entity);
					return new DownloadResult(data, to, new DownloadProgress(
							currentProgress, contentLength));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new DownloadResult(data, to,
				new DownloadProgress(currentProgress, contentLength));
	}
}
