package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.CoastlineObject;
import com.noticemedan.map.model.KDTree.Forest;
import com.noticemedan.map.model.KDTree.ForestCreator;
import com.noticemedan.map.model.KDTree.Rect;
import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectCreater;
import com.noticemedan.map.model.OSMType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.List;

@Slf4j
public class MapCanvas {

	private Forest forest;
	@Getter @Setter
	private Rect viewArea;
	@Getter
	private Canvas canvas;
	private GraphicsContext pen;
	@Getter
	@Setter
	private double zoomLevel;
	private MapObjectCreater moc;

	public MapCanvas() {
		ForestCreator forestCreator = new ForestCreator();
		this.forest = forestCreator.getForest();
		this.viewArea = new Rect(0, 0, 2000, 2000);
		this.canvas = new Canvas(6000, 6000);
		this.pen = canvas.getGraphicsContext2D();
		this.zoomLevel = 1.0;
		moc = MapObjectCreater.getInstance(new Dimension(2600, 1600));
		drawCanvas();
	}

	private void drawCanvas() {
		/** TODO Temporary drawing of coastlines directly from MapObjectCreator
		 *  This is to be fixed
		 *  @Magnus
		 */
		drawCoastlines(moc.getListOfCoastlineObjects());

		drawObjects(forest.rangeSearch(viewArea));
	}

	public void redrawCanvas() {
		pen.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
		drawCanvas();
	}

	private void drawCoastlines(List<CoastlineObject> coastlineObjects) {
		for (CoastlineObject coastlineObject : coastlineObjects) {
			setPenColor(coastlineObject.getColor());
			drawPath(coastlineObject.getPoints());
			pen.fill();
		}
	}

	private void drawObjects(List<MapObject> mapObjects) {
		for(MapObject mapObject : mapObjects) {
			if(mapObject.getOsmType()==OSMType.UNKNOWN) continue;
			if (mapObject.getColor() != null) setPenColor(mapObject.getColor());

			drawPath(mapObject.getPoints());
			if ((isClosed(mapObject))) {
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
