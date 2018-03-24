package com.noticemedan.map.viewmodel;


import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectBuilder;
import com.noticemedan.map.model.OSMType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static javafx.scene.paint.Color.color;

public class MapCanvas {

    @Getter private Canvas canvas;
    private MapObjectBuilder mapObjectBuilder;
    private Map<OSMType,List<MapObject>> mapObjects;
    private GraphicsContext pen;

    public MapCanvas(){
        super();
		this.canvas = new Canvas(50,50);
		this.pen = canvas.getGraphicsContext2D();
		this.mapObjectBuilder = MapObjectBuilder.getInstance(new Dimension(800, 800));
		this.mapObjectBuilder.writeOut();
		this.mapObjects = mapObjectBuilder.getMapObjectsByType();
        drawCanvas();
    }

    private void drawCanvas() {
        setPenAttributes(5,Color.WHITE);
        if (mapContainsKey(OSMType.ROAD))
        	drawObjects(mapObjects.get(OSMType.ROAD));

        setPenAttributes(10.0,Color.ORANGE);
        if (mapContainsKey(OSMType.HIGHWAY))
        	drawObjects(mapObjects.get(OSMType.HIGHWAY));

        setPenAttributes(0.2,Color.LIGHTGRAY);
        if (mapContainsKey(OSMType.BUILDING))
        	drawObjects(mapObjects.get(OSMType.BUILDING));

		setPenAttributes(0.2,Color.GREEN);
		if (mapContainsKey(OSMType.GRASSLAND))
			drawObjects(mapObjects.get(OSMType.GRASSLAND));

		setPenAttributes(0.2,Color.BLUE);
		if (mapContainsKey(OSMType.WATER))
			drawObjects(mapObjects.get(OSMType.WATER));

		setPenAttributes(0.2,Color.YELLOW);
		if (mapContainsKey(OSMType.SAND))
			drawObjects(mapObjects.get(OSMType.SAND));

		setPenAttributes(0.2, Color.rgb(250, 75, 255, 0.2));
		if (mapContainsKey(OSMType.UNKNOWN))
			drawObjects(mapObjects.get(OSMType.UNKNOWN));
    }

    private boolean mapContainsKey(OSMType key) {
    	return mapObjects.containsKey(key);
	}

    private void setPenAttributes(double lineWidth, Color color){
    	pen.setLineWidth(lineWidth);
    	pen.setStroke(color);
    	pen.setFill(color);
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
			putOnCanvas(nextPoint);
			pen.lineTo(nextPoint.getX(), nextPoint.getY());
		}
	}

	private boolean isClosed(MapObject object) {
		return object.getOSMType() != OSMType.ROAD && (object.getOSMType() != OSMType.HIGHWAY);
	}

	private void putOnCanvas(Point2D point){
		if(point.getX()>canvas.getWidth()) resizeCanvasWidth(point.getX());
		if(point.getY()>canvas.getHeight()) resizeCanvasHeight(point.getY());
	}

    private void resizeCanvasWidth(double width){
        this.canvas.setWidth(width);
    }

    private void resizeCanvasHeight(double height){
        this.canvas.setHeight(height);
    }
}
