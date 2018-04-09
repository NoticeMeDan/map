package com.noticemedan.map.model;

import com.noticemedan.map.model.KDTree.Rect;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import lombok.Data;

import java.util.List;

@Data
public class CoastlineObject {
	private List<Point2D> points;
	private Color color;
	private Point2D avgPoint;
	private Rect bounds;
}
