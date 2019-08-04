package com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui;

import com.github.kneelawk.cursemodpackdownloader.cursemeta3.mods.ModDownloadTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.nio.file.Path;

public interface DownloadRequestListener {
    void downloadModpack(Path modpackFile, Path toDir,
                         StringProperty status, StringProperty modLoader, DoubleProperty overallProgress,
                         ObjectProperty<ObservableList<ModDownloadTask>> downloads,
                         BooleanProperty running, BooleanProperty error, int numThreads);
}
