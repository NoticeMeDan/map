package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;

import java.awt.*;
import java.util.ArrayList;

public interface Tree {
	public ArrayList<MapObject> rangeSearch(Point SW, Point NE);
	public MapObject nearestNeighbor(Point queryPoint);
}
