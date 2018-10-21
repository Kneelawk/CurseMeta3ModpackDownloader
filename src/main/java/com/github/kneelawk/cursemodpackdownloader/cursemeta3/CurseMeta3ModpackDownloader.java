package com.github.kneelawk.cursemodpackdownloader.cursemeta3;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.FileModpackParseTask;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.Modpack;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.ModpackDownloadTask;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.RedirectUriSanitizer;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui.DownloaderUI;
import com.google.gson.Gson;

import javafx.application.Application;
import javafx.stage.Stage;

public class CurseMeta3ModpackDownloader extends Application {

	public static void main(String[] args) throws IOException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Gson gson = new Gson();
		CloseableHttpClient client = HttpClients.custom()
				.setRedirectStrategy(new RedirectUriSanitizer()).build();
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
		ExecutorService executor =
				Executors.newSingleThreadExecutor(runnable -> {
					Thread t = new Thread(runnable);
					t.setDaemon(true);
					return t;
				});

		DownloaderUI ui = new DownloaderUI(primaryStage);
		ui.setDownloadRequestListener((modpackFile, toDir, status,
				overallProgress, downloads, running, error, numThreads) -> {
			overallProgress.set(-1);

			FileModpackParseTask fmpt = new FileModpackParseTask(gson, client,
					dbuilder, modpackFile);
			status.bind(fmpt.messageProperty());
			fmpt.setOnFailed(e1 -> {
				fmpt.getException().printStackTrace();
				running.set(false);
			});
			fmpt.setOnSucceeded(e1 -> {
				status.unbind();
				Modpack modpack = fmpt.getModpack();
				ModpackDownloadTask mdt = new ModpackDownloadTask(client, gson,
						modpack, toDir, numThreads);
				status.bind(mdt.messageProperty());
				overallProgress.bind(mdt.progressProperty());
				error.bind(mdt.exceptionProperty().isNotNull());
				downloads.bind(mdt.tasksProperty());
				mdt.setOnFailed(e2 -> {
					mdt.getException().printStackTrace();
					running.set(false);
				});
				mdt.setOnSucceeded(e2 -> {
					status.unbind();
					overallProgress.unbind();
					error.unbind();
					downloads.unbind();
					running.set(false);
				});
				executor.execute(mdt);
			});
			executor.execute(fmpt);
		});
		ui.show();
	}

}
