package com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.ModDownloadTask;
import com.google.common.collect.ImmutableList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DownloaderUI {
	private Stage primaryStage;
	protected FileChooser modpackChooser;
	protected DirectoryChooser outputChooser;
	protected File previousDir = new File(System.getProperty("user.home"));
	protected TextField modpackField;
	protected TextField outputField;
	protected Label statusLabel;
	protected TableView<ModDownloadTask> tasks;
	protected DoubleProperty overallProgress;
	protected BooleanProperty running = new SimpleBooleanProperty(false);
	protected BooleanProperty error = new SimpleBooleanProperty(false);
	protected ReadOnlyObjectProperty<Integer> numThreads;

	protected DownloadRequestListener listener = null;

	public DownloaderUI(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("CurseMeta3 Modpack Downloader");

		modpackChooser = new FileChooser();
		modpackChooser.setTitle("Select A Modpack File");
		modpackChooser.getExtensionFilters().setAll(new ExtensionFilter(
				"Curse Modpack Files", ImmutableList.of("*.zip", "*.bin")));

		outputChooser = new DirectoryChooser();
		outputChooser.setTitle("Select An Output Location");

		VBox root = new VBox();
		root.setSpacing(10);
		root.setPadding(new Insets(25));
		List<String> stylesheets = root.getStylesheets();
		stylesheets.add(getClass().getResource("obsidian/obsidian.css").toExternalForm());
		stylesheets.add(getClass().getResource("style.css").toExternalForm());

		GridPane form = new GridPane();
		form.setAlignment(Pos.TOP_CENTER);
		form.setHgap(10);
		form.setVgap(10);
		root.getChildren().add(form);
		form.disableProperty().bind(running);

		ColumnConstraints cs1 = new ColumnConstraints();
		cs1.setHgrow(Priority.NEVER);
		ColumnConstraints cs2 = new ColumnConstraints();
		cs2.setHgrow(Priority.ALWAYS);

		form.getColumnConstraints().addAll(cs1, cs2, cs1);

		Label modpackLabel = new Label("Modpack Location:");
		form.add(modpackLabel, 0, 0);

		modpackField = new TextField();
		form.add(modpackField, 1, 0);

		Button modpackSelectButton = new Button("...");
		modpackSelectButton.setOnAction(event -> {
			modpackChooser.setInitialDirectory(previousDir);
			File file = modpackChooser.showOpenDialog(primaryStage);
			if (file != null) {
				String path = file.getAbsolutePath();
				modpackField.setText(path);
				previousDir = file.getParentFile();

				if (outputField.getText() == null
						|| "".equals(outputField.getText())) {
					outputField
							.setText(path.substring(0, path.lastIndexOf('.')));
				}
			}
		});
		form.add(modpackSelectButton, 2, 0);

		Label outputLabel = new Label("Output Location:");
		form.add(outputLabel, 0, 1);

		outputField = new TextField();
		form.add(outputField, 1, 1);

		Button outputSelectButton = new Button("...");
		outputSelectButton.setOnAction(event -> {
			outputChooser.setInitialDirectory(previousDir);
			File file = outputChooser.showDialog(primaryStage);
			if (file != null) {
				outputField.setText(file.getAbsolutePath());
				previousDir = file.getParentFile();
			}
		});
		form.add(outputSelectButton, 2, 1);

		Label numThreadsLabel = new Label("Number of download threads:");
		form.add(numThreadsLabel, 0, 2);

		Spinner<Integer> numThreadsSpinner = new Spinner<>(1, 100, 10);
		numThreadsSpinner.setMaxWidth(Double.MAX_VALUE);
		numThreads = numThreadsSpinner.valueProperty();
		form.add(numThreadsSpinner, 1, 2, 2, 1);

		Button downloadButton = new Button("Download Modpack");
		downloadButton.setOnAction(event -> {
			handleModpackDownload();
		});
		GridPane.setHgrow(downloadButton, Priority.ALWAYS);
		downloadButton.setMaxWidth(Double.MAX_VALUE);
		form.add(downloadButton, 0, 3, 3, 1);

		statusLabel = new Label("Not started.");
		statusLabel.setAlignment(Pos.CENTER);
		root.getChildren().add(statusLabel);

		error.addListener((o, oldVal, newVal) -> {
			if (newVal) {
				statusLabel.getStyleClass().add("error-label");
			} else {
				statusLabel.getStyleClass().remove("error-label");
			}
		});

		ProgressBar bar = new ProgressBar();
		bar.setProgress(0);
		bar.setMaxWidth(Double.MAX_VALUE);
		bar.setMinHeight(20);
		overallProgress = bar.progressProperty();
		root.getChildren().add(bar);
		overallProgress.addListener((o, oldVal, newVal) -> {
			if (newVal.doubleValue() >= 1) {
				bar.pseudoClassStateChanged(PseudoClass.getPseudoClass("done"), true);
			} else {
				bar.pseudoClassStateChanged(PseudoClass.getPseudoClass("done"), false);
			}
		});

		tasks = new TableView<>();
		tasks.setMaxHeight(Double.MAX_VALUE);
		tasks.setMaxWidth(Double.MAX_VALUE);
		VBox.setVgrow(tasks, Priority.ALWAYS);
		root.getChildren().add(tasks);

		TableColumn<ModDownloadTask, String> statusColumn =
				new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		statusColumn.setPrefWidth(500);
		tasks.getColumns().add(statusColumn);

		TableColumn<ModDownloadTask, Double> progressColumn =
				new TableColumn<>("Progress");
		progressColumn
				.setCellValueFactory(new PropertyValueFactory<>("progress"));
		progressColumn
				.setCellFactory(ColoredProgressBarTableCell.forTableColumn());
		progressColumn.setPrefWidth(300);
		tasks.getColumns().add(progressColumn);

		TableColumn<ModDownloadTask, String> errorColumn =
				new TableColumn<>("Error");
		errorColumn
				.setCellValueFactory(new PropertyValueFactory<>("exception"));
		errorColumn.setPrefWidth(500);
		tasks.getColumns().add(errorColumn);

		Scene scene = new Scene(root, 1280, 800);
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(500);
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

		running.set(true);

		tasks.getItems().clear();

		if (listener != null) {
			listener.downloadModpack(modpackZip, toDir,
					statusLabel.textProperty(), overallProgress,
					tasks.itemsProperty(), running, error, numThreads.get());
		}
	}

	private void setUserError(TextField field, String error) {
		statusLabel.setText(error);
		statusLabel.getStyleClass().add("error-label");
		field.getStyleClass().add("error-field");
	}

	private void clearUserError(TextField field) {
		statusLabel.setText("Not started.");
		statusLabel.getStyleClass().remove("error-label");
		field.getStyleClass().remove("error-field");
	}

	public void setDownloadRequestListener(DownloadRequestListener listener) {
		this.listener = listener;
	}

	public void show() {
		primaryStage.show();
	}
}
