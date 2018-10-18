package com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileDataJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.FileJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.json.ManifestJson;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.Downloader;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.RedirectUriSanitizer;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class ModpackDownload implements Runnable {
	protected Thread downloadThread;

	protected Path modpackFile;
	protected Path toDir;
	protected StringProperty status;
	protected DoubleProperty overallProgress;
	protected ObservableList<ModDownloadTask> downloads;
	protected BooleanProperty running;
	protected BooleanProperty error;
	protected ExecutorService executor;
	protected int numDownloads;
	protected int successes;
	protected int failures;

	public ModpackDownload(Path modpackFile, Path toDir, StringProperty status,
			DoubleProperty overallProgress,
			ObservableList<ModDownloadTask> downloads, BooleanProperty running,
			BooleanProperty error, int numThreads) {
		this.modpackFile = modpackFile;
		this.toDir = toDir;
		this.status = status;
		this.overallProgress = overallProgress;
		this.downloads = downloads;
		this.running = running;
		this.error = error;

		this.executor = Executors.newFixedThreadPool(numThreads, runnable -> {
			Thread t = new Thread(runnable);
			t.setDaemon(true);
			return t;
		});

		overallProgress.set(0);
		numDownloads = 1;
		successes = 0;
		failures = 0;

		downloadThread = new Thread(this);
	}

	public void start() {
		downloadThread.start();
	}

	public void run() {
		try {
			Gson gson = new Gson();
			CloseableHttpClient client = HttpClients.custom()
					.setRedirectStrategy(new RedirectUriSanitizer()).build();
			Path modsDir = toDir.resolve("mods");
			Files.createDirectories(modsDir);

			Path modpackZip = modpackFile;

			// is modpack an xml?
			FileId id = ModpackXmlParser.parseModpackBin(modpackFile);
			if (id != null) {
				System.out.println("Loaded xml modpack, downloading zip...");
				Platform.runLater(() -> {
					status.set("Loaded xml modpack, downloading zip...");
				});
				modpackZip = Files.createTempFile("modpack", ".zip");
				modpackZip.toFile().deleteOnExit();
				FileDataJson data = AddonUtils.getAddonFile(client, gson,
						id.getProjectId(), id.getFileId());
				Downloader downloader = new Downloader(client,
						data.getDownloadUrl(), modpackZip);
				Platform.runLater(() -> {
					status.bind(downloader.messageProperty());
				});
				downloader.call();
				Platform.runLater(() -> {
					status.unbind();
				});
			}

			System.out.println("Downloadig modpack details...");
			Platform.runLater(() -> {
				status.set("Downloading modpack details...");
			});
			Modpack modpack = new Modpack(modpackZip);
			modpack.parseBaseManifest(gson);

			System.out.println("Extracting overrides...");
			Platform.runLater(() -> {
				status.set("Extracting overrides...");
			});
			modpack.extractOverrides(toDir);

			ManifestJson manifest = modpack.getManifest();
			List<FileJson> files = manifest.getFiles();

			numDownloads = files.size();

			System.out.println("Downloading mods...");
			Platform.runLater(() -> {
				status.set("Downloading mods... 0 / " + numDownloads);
			});
			for (FileJson file : files) {
				Platform.runLater(() -> {
					try {
						ModDownloadTask download = new ModDownloadTask(client, gson,
								manifest.getMinecraft().getVersion(), file,
								modsDir);
						downloads.add(download);
						download.setOnSucceeded(event -> {
							successes++;
							overallProgress.set(((double) successes)
									/ ((double) numDownloads));
							status.set(
									String.format("Downloading mods... %d / %d",
											successes, numDownloads));

							if (successes + failures >= numDownloads) {
								status.set(String.format(
										"Done. %d / %d Mods downloaded.",
										successes, numDownloads));
								running.set(false);
							}
						});
						download.setOnFailed(event -> {
							failures++;

							if (successes + failures >= numDownloads) {
								status.set(String.format(
										"Done. %d / %d Mods downloaded.",
										successes, numDownloads));
								running.set(false);
							}
						});
						executor.execute(download);
					} catch (Exception e) {
						e.printStackTrace();
						status.set(e.toString());
						error.set(true);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			Platform.runLater(() -> {
				status.set(e.toString());
				error.set(true);
			});
		}
	}
}
