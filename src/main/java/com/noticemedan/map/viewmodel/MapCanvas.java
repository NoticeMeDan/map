package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectBuilderInterface;
import com.noticemedan.map.model.OSMType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.Getter;
import java.util.List;
import java.util.Map;

public class MapCanvas {

    @Getter private Canvas canvas;
    private MapObjectBuilderInterface mapObjectBuilderInterface;
    private Map<OSMType,List<MapObject>> mapObjects;
    private GraphicsContext pen;
    private MapModel model;

    public MapCanvas(){
        super();
        initializeFields();
        drawCanvas();
    }

    private void initializeFields(){
		this.canvas = new Canvas(500,500);
		this.pen = canvas.getGraphicsContext2D();
		this.model = new MapModel();
		this.mapObjectBuilderInterface = new MapObjectBuilderInterface() {
			@Override
			public Map<OSMType, List<MapObject>> getMapObjectsByType() {
				return null;
			}
		};
		this.mapObjects = mapObjectBuilderInterface.getMapObjectsByType();

	}

    private void drawCanvas() {

        /*setPenAttributes(0.5,Color.WHITE);
        drawObjects(mapObjects.get(OSMType.ROAD));

        setPenAttributes(1.0,Color.ORANGE);
        drawObjects(mapObjects.get(OSMType.HIGHWAY));

        setPenAttributes(0.2,Color.GRAY);
        drawObjects(mapObjects.get(OSMType.BUILDING));

		setPenAttributes(0.2,Color.GREEN);
		drawObjects(mapObjects.get(OSMType.GRASSLAND));

		setPenAttributes(0.2,Color.BLUE);
		drawObjects(mapObjects.get(OSMType.WATER));

		setPenAttributes(0.2,Color.YELLOW);
		drawObjects(mapObjects.get(OSMType.SAND));*/
    }

    private void setPenAttributes(double lineWidth, Color color){
    	pen.setLineWidth(lineWidth);
    	pen.setStroke(color);
	}

	private void drawObjects(List<MapObject> objects){
    	for(MapObject object : objects){
    		List<Point2D> points = object.getPoints();
    		drawPath(points);
			if ((isClosed(object))) {
				pen.closePath();
				pen.fill();
			} else pen.stroke();
		}
    }

	private void drawPath(List<Point2D> points) {
		Point2D startPoint = points.get(0);
		pen.beginPath();
		pen.moveTo(startPoint.getX(), startPoint.getY());
		for (int i = 1; i < points.size(); i++) {
			Point2D nextPoint = points.get(i);
			pen.lineTo(nextPoint.getX(), nextPoint.getY());
		}
	}

	private boolean isClosed(MapObject object) {
		return object.getOSMType() != OSMType.ROAD && (object.getOSMType() != OSMType.HIGHWAY);
	}

    private void resizeCanvasWidth(double width){
        this.canvas.setWidth(width);
    }

    private void resizeCanvasHeight(double height){
        this.canvas.setHeight(height);
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
}
