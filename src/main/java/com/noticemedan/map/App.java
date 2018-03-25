package com.noticemedan.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
		Image icon = new Image(App.class.getResourceAsStream("/media/icon.png"));
		primaryStage.setTitle("Mappr");
		primaryStage.getIcons().add(icon);
		primaryStage.setWidth(1100);
		primaryStage.setHeight(650);
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
    }
}
