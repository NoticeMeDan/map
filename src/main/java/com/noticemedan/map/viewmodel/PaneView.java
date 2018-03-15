package com.noticemedan.map.viewmodel;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import lombok.Getter;

public class PaneView extends ImageView {

    private static PaneView instance;

    private MapModel mapModel;
    @Getter private Pane rootPane;

    private PaneView() {
        this.mapModel = new MapModel("lines20k.txt");
        this.rootPane = new Pane();
        drawPaths();
    }

    public static PaneView getInstance() {
        if (instance == null) instance = new PaneView();
        return instance;
    }

    private void drawPaths() {
        for (Path path : this.mapModel) {
            this.rootPane.getChildren().addAll(path);
        }
    }

}
