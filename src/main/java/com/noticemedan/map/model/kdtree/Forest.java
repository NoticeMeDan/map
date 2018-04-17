package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.utilities.Rect;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Forest implements ForestInterface {
	private KDTree trees[];

	@Override
	public List<OSMMaterialElement> rangeSearch(Rect searchQuery, int zoomLevel) {
		ArrayList searchResults = new ArrayList<>();
		for (int i = 0; i < zoomLevel+1; i++) {
			searchResults.addAll(trees[i].rangeSearch(searchQuery));
		}
		return searchResults;
	}

	//Range search as if only having one zoom level.
	public List<OSMMaterialElement> rangeSearch(Rect searchQuery) {
		return rangeSearch(searchQuery, trees.length-1);
	}

	@Override
	public OSMMaterialElement nearestNeighbor(double x, double y) {
		throw new RuntimeException("nearestNeighbor() not implemented yet.");
	}
}
