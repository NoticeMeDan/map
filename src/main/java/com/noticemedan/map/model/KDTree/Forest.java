package com.noticemedan.map.model.KDTree;

import java.util.ArrayList;

public class Forest implements ForestInterface {
	KDTree[] trees;

	public Forest(int N, double[] zoomBreakPoints) {
		if(N != zoomBreakPoints.length) new RuntimeException("Number of trees and zoom break points should be equal.");
		trees = new KDTree[N];
	}



	@Override
	public ArrayList<KDMapObject> rangeSearch(Rect query) {
		new RuntimeException("rangeSearch() not implemented yet.");
		return null;
	}

	@Override
	public KDMapObject nearestNeighbor(double x, double y) {
		new RuntimeException("nearestNeighbor() not implemented yet.");
		return null;
	}
}
