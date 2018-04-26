package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Rect;

import java.util.List;

public interface ForestInterface {
	public List<OsmElement> rangeSearch(Rect query, double zoomLevel);
	public OsmElement nearestNeighbor(double x, double y);
}
