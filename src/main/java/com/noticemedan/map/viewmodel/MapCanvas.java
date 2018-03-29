package com.noticemedan.map.viewmodel;


import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectCreater;
import com.noticemedan.map.model.OSMType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
public class MapCanvas {

    @Getter private Canvas canvas;
    private MapObjectCreater mapObjectCreater;
    private Map<OSMType,List<MapObject>> mapObjects;
    private GraphicsContext pen;

    public MapCanvas(Dimension dim) {
        super();
		this.canvas = new Canvas(50, 50);
		this.pen = canvas.getGraphicsContext2D();
		this.mapObjectCreater = MapObjectCreater.getInstance(dim);
		this.mapObjects = mapObjectCreater.getMapObjectsByType();
        drawCanvas();
    }

    private void drawCanvas() {
		/* setPenAttributes(20, Color.ORANGE);
		if (mapContainsKey(OSMType.COASTLINE))
			drawObjects(mapObjects.get(OSMType.COASTLINE));*/

		/* setPenAttributes(0.2, Color.rgb(250, 75, 255, 0.2));
		if (mapContainsKey(OSMType.UNKNOWN))
			drawObjects(mapObjects.get(OSMType.UNKNOWN)); */

        setPenAttributes(5,Color.LIGHTGRAY);
        if (mapContainsKey(OSMType.ROAD))
        	drawObjects(mapObjects.get(OSMType.ROAD));

        setPenAttributes(10.0,Color.ORANGE);
        if (mapContainsKey(OSMType.HIGHWAY))
        	drawObjects(mapObjects.get(OSMType.HIGHWAY));

        setPenAttributes(0.2,Color.rgb(125, 125,125));
        if (mapContainsKey(OSMType.BUILDING))
        	drawObjects(mapObjects.get(OSMType.BUILDING));

		setPenAttributes(0.2,Color.rgb(12,158,89));
		if (mapContainsKey(OSMType.GRASSLAND))
			drawObjects(mapObjects.get(OSMType.GRASSLAND));

		setPenAttributes(0.2, Color.rgb(40,84,210));
		if (mapContainsKey(OSMType.WATER))
			drawObjects(mapObjects.get(OSMType.WATER));

		setPenAttributes(0.2,Color.YELLOW);
		if (mapContainsKey(OSMType.SAND))
			drawObjects(mapObjects.get(OSMType.SAND));

		setPenAttributes(0.2,Color.rgb(12,158,89));
		if (mapContainsKey(OSMType.TREE_ROW))
			drawObjects(mapObjects.get(OSMType.TREE_ROW));

		setPenAttributes(0.2,Color.rgb(73,150,128));
		if (mapContainsKey(OSMType.HEATH))
			drawObjects(mapObjects.get(OSMType.HEATH));

		setPenAttributes(0.2,Color.rgb(88,232,93));
		if (mapContainsKey(OSMType.PLAYGROUND))
			drawObjects(mapObjects.get(OSMType.PLAYGROUND));

		setPenAttributes(0.2,Color.rgb(14,184,118));
		if (mapContainsKey(OSMType.GARDEN))
			drawObjects(mapObjects.get(OSMType.GARDEN));

		setPenAttributes(0.2,Color.rgb(14,184,118));
		if (mapContainsKey(OSMType.PARK))
			drawObjects(mapObjects.get(OSMType.PARK));
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
		return object.getOsmType() != OSMType.ROAD && (object.getOsmType() != OSMType.HIGHWAY /*&& (object.getOsmType() != OSMType.COASTLINE)*/);
	}

	private void putOnCanvas(Point2D point) {
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
