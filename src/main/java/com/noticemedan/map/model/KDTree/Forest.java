package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


public class Forest implements ForestInterface {
	@Setter private KDTree tree;

	@Override
	public List<MapObject> rangeSearch(Rect query) {
		return tree.rangeSearch(query);
	}

	@Override
	public MapObject nearestNeighbor(double x, double y) {
		throw new RuntimeException("nearestNeighbor() not implemented yet.");
	}
}
