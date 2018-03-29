package com.noticemedan.map.model.KDTree;

import java.util.Comparator;

public class POINT_SORT_Y implements Comparator<KDTreePoint> {
	@Override
	public int compare(KDTreePoint a, KDTreePoint b) {
		if (a.getY() > b.getY()) return 1;
		if (a.getY() < b.getY()) return -1;
		return 0;
	}
}
