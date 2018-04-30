package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.Rect;

import java.util.List;

public interface ForestInterface {
	public List<OsmElement> rangeSearch(Rect query, double zoomLevel);
	public OsmElement nearestNeighbor(Coordinate queryPoint, double zoomLevel);
}
