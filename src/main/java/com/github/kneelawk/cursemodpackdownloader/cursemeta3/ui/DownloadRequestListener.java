package com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui;

import java.nio.file.Path;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.ModDownloadTask;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface DownloadRequestListener {
	public void downloadModpack(Path modpackFile, Path toDir,
			StringProperty status, DoubleProperty overallProgress,
			ObservableList<ModDownloadTask> downloads, BooleanProperty running,
			BooleanProperty error, int numThreads);
}
