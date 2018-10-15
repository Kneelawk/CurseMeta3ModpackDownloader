package com.github.kneelawk.cursemodpackdownloader.cursemeta3;

import java.io.IOException;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.modpack.ModpackDownload;
import com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui.DownloaderUI;

import javafx.application.Application;
import javafx.stage.Stage;

public class CurseMeta3ModpackDownloader extends Application {

	public static void main(String[] args) throws IOException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		DownloaderUI ui = new DownloaderUI(primaryStage);
		ui.setDownloadRequestListener((modpackFile, toDir, status,
				overallProgress, downloads, running, error, numThreads) -> {
			ModpackDownload download = new ModpackDownload(modpackFile, toDir,
					status, overallProgress, downloads, running, error,
					numThreads);
			download.start();
		});
		ui.show();
	}

}
