package com.github.kneelawk.cursemodpackdownloader.cursemeta3;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.FileModpackParseTask;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.Modpack;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.ModpackDownloadTask;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.net.ClientManager;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui.DownloaderUI;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurseMeta3ModpackDownloader extends Application {

	public static void main(String[] args) throws IOException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientManager clientManager = new ClientManager();
		Gson gson = new Gson();
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

			FileModpackParseTask fmpt = new FileModpackParseTask(gson,
					clientManager, dbuilder, modpackFile);
			status.bind(fmpt.messageProperty());
			fmpt.setOnFailed(e1 -> {
				fmpt.getException().printStackTrace();
				status.unbind();
				running.set(false);
			});
			fmpt.setOnSucceeded(e1 -> {
				status.unbind();
				Modpack modpack = fmpt.getModpack();
				ModpackDownloadTask mdt = new ModpackDownloadTask(clientManager,
						gson, modpack, toDir, numThreads);
				status.bind(mdt.messageProperty());
				overallProgress.bind(mdt.progressProperty());
				error.bind(mdt.exceptionProperty().isNotNull());
				downloads.bind(mdt.tasksProperty());
				mdt.setOnFailed(e2 -> {
					mdt.getException().printStackTrace();
					status.unbind();
					overallProgress.unbind();
					error.unbind();
					downloads.unbind();
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
