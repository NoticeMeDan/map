package com.noticemedan.map.model;

import com.noticemedan.map.model.Utilities.Rect;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.Data;

import java.util.List;

@Data
public class OSMCoastlineElement {
	private List<Point2D> points;
	private Color color;
	private Point2D avgPoint;
	private Rect bounds;
}
