package com.noticemedan.map.viewmodel;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Line;
import lombok.Getter;

public class MapCanvas {

    @Getter private Canvas canvas;
    private GraphicsContext pen;
    private MapModel model;

    public MapCanvas(){
        super();
        this.canvas = new Canvas(500,500);
        this.pen = canvas.getGraphicsContext2D();
        this.model = new MapModel();
        drawCanvas();
    }

    public void drawCanvas(){
        pen.setLineWidth(0.2);
        drawLines();
    }

    private void drawLines(){
        for(Line line : model.getLines()){
            double x1 = line.getStartX();
            double y1 = line.getStartY();
            double x2 = line.getEndX();
            double y2 = line.getEndY();

            if(x2>canvas.getWidth()) resizeCanvasWidth(x2);
            if(y2>canvas.getHeight()) resizeCanvasHeight(y2);
            pen.strokeLine(x1,y1,x2,y2);
        }
    }

    private void resizeCanvasWidth(double width){
        this.canvas.setWidth(width);
    }

    private void resizeCanvasHeight(double height){
        this.canvas.setHeight(height);
    }
}
