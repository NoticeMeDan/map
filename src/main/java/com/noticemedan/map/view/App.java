package com.noticemedan.map.view;

import com.noticemedan.map.viewmodel.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        PaneView paneView = new PaneView();
        primaryStage.setTitle("SimpleMap");
        primaryStage.setScene(new Scene(paneView.getRoot()));
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
