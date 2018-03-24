package com.noticemedan.map.view;

import com.noticemedan.map.viewmodel.MouseController;
import com.noticemedan.map.viewmodel.CustomPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CustomPane root = new CustomPane(new Dimension(900, 450));
        MouseController controller = new MouseController();
        controller.addZoomAbility(root);
        primaryStage.setTitle("Map");
        primaryStage.setScene(new Scene(root));
        root.setStyle("-fx-background-color: blue");
        primaryStage.show();
    }
}
