package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import java.util.List;

public interface ForestInterface {
	public List<MapObject> rangeSearch(Rect query, int zoomLevel);
	public MapObject nearestNeighbor(double x, double y);
}
