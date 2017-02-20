package com.norcom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		GridPane grid;
		grid = FXMLLoader.load(getClass().getClassLoader().getResource("UI.fxml"));
		grid.getStylesheets().addAll(getClass().getClassLoader().getResource("lineChart.css").toExternalForm());
		Scene scene = new Scene(grid, 800, 600);

		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.setResizable(false);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}