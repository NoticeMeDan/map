package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;

import java.util.ArrayList;

// TODO bedre interface!
public interface Tree {
	public ArrayList<KDTreePoint> rangeSearch(double lx, double ly, double hx, double hy);
	//public MapObject nearestNeighbor(Point queryPoint);
}
