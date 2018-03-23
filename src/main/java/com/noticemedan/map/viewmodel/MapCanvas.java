package com.noticemedan.map.viewmodel;


import com.noticemedan.map.model.DemoObject;
import com.noticemedan.map.model.DemoObjectBuilder;
import com.noticemedan.map.model.OSMType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import java.util.List;
import java.util.Map;

public class MapCanvas {

    @Getter private Canvas canvas;
    private DemoObjectBuilder mapObjectBuilderInterface;
    private Map<OSMType,List<DemoObject>> mapObjects;
    private GraphicsContext pen;

    public MapCanvas(){
        super();
		this.canvas = new Canvas(50,50);
		this.pen = canvas.getGraphicsContext2D();
		this.mapObjectBuilderInterface = new DemoObjectBuilder();
		this.mapObjects = mapObjectBuilderInterface.getMapObjectsByType();
        drawCanvas();
    }

    private void drawCanvas() {
        setPenAttributes(5,Color.WHITE);
        drawObjects(mapObjects.get(OSMType.ROAD));

        setPenAttributes(10.0,Color.ORANGE);
        drawObjects(mapObjects.get(OSMType.HIGHWAY));
		/*
        setPenAttributes(0.2,Color.LIGHTGRAY);
        drawObjects(mapObjects.get(OSMType.BUILDINGS));

		setPenAttributes(0.2,Color.GREEN);
		drawObjects(mapObjects.get(OSMType.GRASSLAND));

		setPenAttributes(0.2,Color.BLUE);
		drawObjects(mapObjects.get(OSMType.WATER));
		*/
		setPenAttributes(0.2,Color.YELLOW);
		drawObjects(mapObjects.get(OSMType.SAND));
    }

    private void setPenAttributes(double lineWidth, Color color){
    	pen.setLineWidth(lineWidth);
    	pen.setStroke(color);
    	pen.setFill(color);
	}

	private void drawObjects(List<DemoObject> objects){
    	for(DemoObject object : objects){
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

	private boolean isClosed(DemoObject object) {
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
