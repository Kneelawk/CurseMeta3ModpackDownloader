package com.github.kneelawk.cursemodpackdownloader.cursemeta3.net;

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

public class Downloader extends Task<DownloadResult> {
	private CloseableHttpClient client;
	private String from;
	private String filename;
	private Path to;

	private long currentProgress;
	private long contentLength;

	public Downloader(CloseableHttpClient client, String from, Path to) {
		this.client = client;
		this.from = from;
		this.to = to;
		this.filename = from.substring(from.lastIndexOf('/') + 1);

		updateMessage("Waiting " + filename + "...");
	}

	@Override
	public DownloadResult call() throws Exception {
		HttpGet request = new HttpGet(CurseURIUtils.sanitizeUri(from, true));
		try (CloseableHttpResponse response = client.execute(request)) {
			HttpEntity entity = response.getEntity();
			contentLength = entity.getContentLength();
			InputStream is = entity.getContent();
			OutputStream os = Files.newOutputStream(to);

			updateMessage("Downloading " + filename + "... 0%");
			updateProgress(0, contentLength);

			currentProgress = 0;
			byte[] buf = new byte[8192];
			int len;
			while ((len = is.read(buf)) >= 0) {
				os.write(buf, 0, len);

				currentProgress += len;
				updateMessage(String.format("Downloading %s... %.1f%%",
						filename, ((double) currentProgress)
								/ ((double) contentLength) * 100));
				updateProgress(currentProgress, contentLength);

				if (isCancelled()) {
					EntityUtils.consume(entity);
					return new DownloadResult(from, to, new DownloadProgress(
							currentProgress, contentLength));
				}
			}
		}

		return new DownloadResult(from, to,
				new DownloadProgress(currentProgress, contentLength));
	}
}
