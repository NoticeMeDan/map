package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Forest implements ForestInterface {
	private KDTree trees[];

	@Override
	public List<MapObject> rangeSearch(Rect query, int zoomLevel) {
		ArrayList searchResults = new ArrayList<>();
		for (int i = 0; i < zoomLevel+1; i++) {
			searchResults.addAll(trees[i].rangeSearch(query));
		}
		return searchResults;
	}

	@Override
	public MapObject nearestNeighbor(double x, double y) {
		throw new RuntimeException("nearestNeighbor() not implemented yet.");
	}
}
