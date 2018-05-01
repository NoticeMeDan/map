package com.noticemedan.mappr.model.kdtree;

import com.noticemedan.mappr.model.osm.Element;
import com.noticemedan.mappr.model.util.Rect;

import java.util.List;

public interface ForestInterface {
	public List<Element> rangeSearch(Rect query, double zoomLevel);
	public Element nearestNeighbor(double x, double y);
}
