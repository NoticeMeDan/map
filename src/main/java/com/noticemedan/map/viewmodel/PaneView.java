package com.noticemedan.map.viewmodel;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import lombok.Getter;

public class PaneView {

    private MapModel mapModel;
    @Getter private Pane root;

    public PaneView() {
        this.mapModel = new MapModel("lines8.txt");
        this.root = new Pane();
        drawPaths();
    }

    private void drawPaths() {
        for (Path path : this.mapModel) {
            this.root.getChildren().addAll(path);
        }
    }

}
