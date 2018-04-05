package com.noticemedan.map.model.KDTree;

import java.util.ArrayList;

public interface ForestInterface {
	public ArrayList<KDMapObject> rangeSearch(Rect query);
	public KDMapObject nearestNeighbor(double x, double y);
}
