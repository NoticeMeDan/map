package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.osm.Element;
import com.noticemedan.map.model.osm.Element;
import com.noticemedan.map.model.utilities.Rect;

import java.util.List;

public interface ForestInterface {
	public List<Element> rangeSearch(Rect query, double zoomLevel);
	public Element nearestNeighbor(double x, double y);
}
