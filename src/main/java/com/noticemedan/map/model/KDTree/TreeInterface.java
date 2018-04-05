package com.noticemedan.map.model.KDTree;

import java.util.ArrayList;

// TODO bedre interface!
public interface TreeInterface {
	public ArrayList<KDMapObject> rangeSearch(double lx, double ly, double hx, double hy);
	//public MapObject nearestNeighbor(Point queryPoint);
}
