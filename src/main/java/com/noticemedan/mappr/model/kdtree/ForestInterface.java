package com.noticemedan.mappr.model.kdtree;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.collection.Vector;

public interface ForestInterface {
	public Vector<Element> rangeSearch(Rect query, double zoomLevel);
	public Element nearestNeighbor(Coordinate queryPoint, double zoomLevel);
}
