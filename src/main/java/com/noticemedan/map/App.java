package com.noticemedan.map;

import com.noticemedan.map.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {
	public static void main(String[] args) {
		launch(args);
	}

    @Override
    public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
		AnchorPane mainView = fxmlLoader.load();
		MainViewController mainViewController = fxmlLoader.getController();
		mainViewController.setStage(primaryStage);
		mainViewController.setUpStage();
    }
}
