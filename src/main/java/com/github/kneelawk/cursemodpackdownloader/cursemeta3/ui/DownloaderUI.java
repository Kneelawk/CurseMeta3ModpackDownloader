package com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.collect.ImmutableList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class DownloaderUI {
	private Stage primaryStage;
	protected FileChooser modpackChooser;
	protected DirectoryChooser outputChooser;
	protected File previousDir = new File(System.getProperty("user.home"));
	protected TextField modpackField;
	protected TextField outputField;
	protected Label errorLabel;

	protected DownloadRequestListener listener = null;

	public DownloaderUI(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("CurseMeta3 Modpack Downloader");

		modpackChooser = new FileChooser();
		modpackChooser.setTitle("Select a modpack zip");
		modpackChooser.getExtensionFilters().setAll(new ExtensionFilter(
				"Curse Modpack Files", ImmutableList.of("*.zip", "*.bin")));

		outputChooser = new DirectoryChooser();
		outputChooser.setTitle("Select an output location");

		GridPane root = new GridPane();
		root.setAlignment(Pos.TOP_CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25));

		ColumnConstraints cs1 = new ColumnConstraints();
		cs1.setHgrow(Priority.NEVER);
		ColumnConstraints cs2 = new ColumnConstraints();
		cs2.setHgrow(Priority.ALWAYS);

		root.getColumnConstraints().addAll(cs1, cs2, cs1);

		Label modpackLabel = new Label("Modpack Location:");
		root.add(modpackLabel, 0, 0);

		modpackField = new TextField();
		root.add(modpackField, 1, 0);

		Button modpackSelectButton = new Button("...");
		modpackSelectButton.setOnAction(event -> {
			modpackChooser.setInitialDirectory(previousDir);
			File file = modpackChooser.showOpenDialog(primaryStage);
			if (file != null) {
				modpackField.setText(file.getAbsolutePath());
				previousDir = file.getParentFile();
			}
		});
		root.add(modpackSelectButton, 2, 0);

		Label outputLabel = new Label("Output Location:");
		root.add(outputLabel, 0, 1);

		outputField = new TextField();
		root.add(outputField, 1, 1);

		Button outputSelectButton = new Button("...");
		outputSelectButton.setOnAction(event -> {
			outputChooser.setInitialDirectory(previousDir);
			File file = outputChooser.showDialog(primaryStage);
			if (file != null) {
				outputField.setText(file.getAbsolutePath());
				previousDir = file.getParentFile();
			}
		});
		root.add(outputSelectButton, 2, 1);

		Button downloadButton = new Button("Download Modpack");
		downloadButton.setOnAction(event -> {
			handleModpackDownload();
		});
		GridPane.setHgrow(downloadButton, Priority.ALWAYS);
		downloadButton.setMaxWidth(Double.MAX_VALUE);
		root.add(downloadButton, 0, 2, 3, 1);

		errorLabel = new Label();
		errorLabel.setAlignment(Pos.CENTER);
		errorLabel.setTextFill(Color.FIREBRICK);
		root.add(errorLabel, 0, 3, 3, 1);

		Scene scene = new Scene(root, 600, 500);
		primaryStage.setScene(scene);
	}

	private void handleModpackDownload() {
		String modpack = modpackField.getText();
		String output = outputField.getText();

		if (modpack == null || "".equals(modpack)) {
			setUserError(modpackField, "The Modpack Location field is empty.");
			return;
		}

		if (output == null || "".equals(output)) {
			setUserError(outputField, "The Output Location field is empty.");
			return;
		}

		Path modpackZip = Paths.get(modpackField.getText());
		Path toDir = Paths.get(outputField.getText());

		if (!Files.exists(modpackZip)) {
			setUserError(modpackField, "The modpack zip doesn't exist.");
			return;
		}

		clearUserError(modpackField);
		clearUserError(outputField);

		if (listener != null) {
			listener.downloadModpack(modpackZip, toDir);
		}
	}

	private void setUserError(TextField field, String error) {
		errorLabel.setText(error);
		field.setBorder(new Border(new BorderStroke(Color.FIREBRICK,
				BorderStrokeStyle.SOLID, null, null)));
	}

	private void clearUserError(TextField field) {
		errorLabel.setText("");
		field.setBorder(null);
	}

	public void setDownloadRequestListener(DownloadRequestListener listener) {
		this.listener = listener;
	}

	public void show() {
		primaryStage.show();
	}
}
