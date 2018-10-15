package com.github.kneelawk.cursemodpackdownloader.cursemeta3.ui;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ColoredProgressBarTableCell<S> extends TableCell<S, Double> {
	public static <S> Callback<TableColumn<S, Double>, TableCell<S, Double>> forTableColumn() {
		return param -> new ColoredProgressBarTableCell<S>();
	}

	protected final ProgressBar progressBar;
	protected ObservableValue<Double> observable;

	public ColoredProgressBarTableCell() {
		getStyleClass().add("progress-bar-table-cell");

		progressBar = new ProgressBar();
		progressBar.setMaxWidth(Double.MAX_VALUE);
	}

	@Override
	protected void updateItem(Double item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setGraphic(null);
		} else {
			progressBar.progressProperty().unbind();

			final TableColumn<S, Double> column = getTableColumn();
			observable = column == null ? null
					: column.getCellObservableValue(getIndex());

			if (observable != null) {
				progressBar.progressProperty().bind(observable);
				if (observable.getValue() >= 1) {
					progressBar.getStyleClass().add("progress-bar-done");
				} else {
					progressBar.getStyleClass().remove("progress-bar-done");
				}
			} else if (item != null) {
				progressBar.setProgress(item);
				if (item >= 1) {
					progressBar.getStyleClass().add("progress-bar-done");
				} else {
					progressBar.getStyleClass().remove("progress-bar-done");
				}
			}

			setGraphic(progressBar);
		}
	}
}
