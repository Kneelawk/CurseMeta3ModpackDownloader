package com.github.kneelawk.cursemodpackdownloader.cursemeta3;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

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
		/*
		 * Fix for weird ssl errors provided by:
		 * https://stackoverflow.com/a/52000542
		 */
		SSLContext sslContext = SSLContext.getDefault();
		SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(
				sslContext, new String[] { "TLSv1.2" }, null,
				NoopHostnameVerifier.INSTANCE);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory>create()
				.register("https", connectionSocketFactory)
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.build();
		HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
				registry);

		CloseableHttpClient client = HttpClients.custom()
				.setRedirectStrategy(new RedirectUriSanitizer())
				.setSSLSocketFactory(connectionSocketFactory)
				.setConnectionManager(connectionManager).build();

		Gson gson = new Gson();
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();
		ExecutorService executor = Executors
				.newSingleThreadExecutor(runnable -> {
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
				status.unbind();
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
