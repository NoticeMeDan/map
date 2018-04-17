package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.utilities.Rect;

import java.util.List;

public interface ForestInterface {
	public List<OSMMaterialElement> rangeSearch(Rect query, int zoomLevel);

	public OSMMaterialElement nearestNeighbor(double x, double y);
}
