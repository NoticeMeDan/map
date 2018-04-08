package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Forest;
import com.noticemedan.map.model.KDTree.ForestCreator;
import com.noticemedan.map.model.KDTree.Rect;
import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.OSMType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class MapCanvas {

	@Getter private Canvas canvas;
	private GraphicsContext pen;
	private ForestCreator forestCreator;
	private Forest forest;
	@Getter @Setter
	private double zoomLevel;
	@Getter @Setter
	private Rect viewArea;

	public MapCanvas() {
		this.canvas = new Canvas(6000, 6000);
		this.pen = canvas.getGraphicsContext2D();
		this.forestCreator = new ForestCreator();
		this.forest = forestCreator.getForest();
		this.zoomLevel = 1.0;
		this.viewArea = new Rect(0,0,2000,2000);
		drawCanvas();
	}

	private void drawCanvas() {
		pen.setStroke(Color.WHITE);
		pen.setFill(Color.WHITE);
		pen.setLineWidth(2);
		drawObjects(forest.rangeSearch(viewArea));
	}

	public void redrawCanvas() {
		pen.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
		drawCanvas();
	}

	private void drawObjects(List<MapObject> mapObjects) {
		for(MapObject mapObject : mapObjects) {
			if(mapObject.getOsmType()==OSMType.UNKNOWN) continue;
			if(mapObject.getColor()!=null) setPenColor(mapObject.getColor()); //IF-STATEMENT UNTIL MapObject getColor() WORKS PROPERLY

			drawPath(mapObject.getPoints());
			if ((isClosed(mapObject))) {
				pen.closePath();
				pen.fill();
			} else pen.stroke();
		}
	}

	private void drawPath(List<Point2D> points) {
		Point2D startPoint = points.get(0);
		pen.beginPath();
		pen.moveTo(startPoint.getX()*this.zoomLevel, startPoint.getY()*this.zoomLevel);
		for (int i = 1; i < points.size(); i++) {
			Point2D nextPoint = points.get(i);
			pen.lineTo(nextPoint.getX()*this.zoomLevel, nextPoint.getY()*this.zoomLevel);
		}
	}

	private void setPenColor(Color color) {
		pen.setStroke(color);
		pen.setFill(color);
	}

	//HOT FIX UNTIL MapObject isOpen() IS WORKING
	//TODO set boolean isOpen() when creating new MapObject.
	private boolean isClosed(MapObject mapObject) {
		return mapObject.getOsmType() != OSMType.ROAD && (mapObject.getOsmType() != OSMType.HIGHWAY && (mapObject.getOsmType() != OSMType.COASTLINE));
	}
}
