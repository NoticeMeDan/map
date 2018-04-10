package com.noticemedan.map.viewmodel;

import com.noticemedan.map.model.KDTree.Forest;
import com.noticemedan.map.model.KDTree.ForestCreator;
import com.noticemedan.map.model.OSMCoastlineElement;
import com.noticemedan.map.model.OSMElementCreator;
import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.OSMType;
import com.noticemedan.map.model.Utilities.Rect;
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
public class OSMCanvas {

	private Forest forest;
	@Getter @Setter
	private Rect viewArea;
	@Getter
	private Canvas canvas;
	private GraphicsContext pen;
	@Getter
	@Setter
	private double zoomLevel;
	private OSMElementCreator moc;

	public OSMCanvas() {
		ForestCreator forestCreator = new ForestCreator();
		this.forest = forestCreator.getForest();
		this.viewArea = new Rect(0, 0, 2000, 2000);
		this.canvas = new Canvas(6000, 6000);
		this.pen = canvas.getGraphicsContext2D();
		this.zoomLevel = 1.0;
		moc = OSMElementCreator.getInstance(new Dimension(2600, 1600));
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
		pen.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		drawCanvas();
	}

	private void drawCoastlines(List<OSMCoastlineElement> coastlineObjects) {
		for (OSMCoastlineElement coastlineObject : coastlineObjects) {
			setPenColor(coastlineObject.getColor());
			drawPath(coastlineObject.getPoints());
			pen.fill();
		}
	}

	private void drawObjects(List<OSMMaterialElement> osmMaterialElements) {
		for (OSMMaterialElement osmMaterialElement : osmMaterialElements) {
			if (osmMaterialElement.getOsmType() == OSMType.UNKNOWN) continue;
			if (osmMaterialElement.getColor() != null) setPenColor(osmMaterialElement.getColor());

			drawPath(osmMaterialElement.getPoints());
			if ((isClosed(osmMaterialElement))) {
				pen.fill();
			} else pen.stroke();
		}
	}

	private void drawPath(List<Point2D> points) {
		Point2D startPoint = points.get(0);
		pen.beginPath();
		pen.moveTo(startPoint.getX() * this.zoomLevel, startPoint.getY() * this.zoomLevel);
		for (int i = 1; i < points.size(); i++) {
			Point2D nextPoint = points.get(i);
			pen.lineTo(nextPoint.getX() * this.zoomLevel, nextPoint.getY() * this.zoomLevel);
		}
	}

	private void setPenColor(Color color) {
		pen.setStroke(color);
		pen.setFill(color);
	}

	//HOT FIX UNTIL OSMMaterialElement isOpen() IS WORKING
	//TODO set boolean isOpen() when creating new OSMMaterialElement.
	private boolean isClosed(OSMMaterialElement osmMaterialElement) {
		return osmMaterialElement.getOsmType() != OSMType.ROAD && (osmMaterialElement.getOsmType() != OSMType.HIGHWAY && (osmMaterialElement.getOsmType() != OSMType.COASTLINE));
	}
}
