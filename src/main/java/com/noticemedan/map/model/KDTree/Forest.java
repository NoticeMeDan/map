package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectCreater;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;


public class Forest implements ForestInterface {
	@Getter private Forest forest;
	@Setter private KDTree tree;

	@Override
	public ArrayList<KDMapObject> rangeSearch(Rect query) {
		throw new RuntimeException("rangeSearch() not implemented yet.");
	}

	@Override
	public KDMapObject nearestNeighbor(double x, double y) {
		throw new RuntimeException("nearestNeighbor() not implemented yet.");
	}
}
