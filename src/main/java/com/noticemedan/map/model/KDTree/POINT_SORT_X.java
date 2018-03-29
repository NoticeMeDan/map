package com.noticemedan.map.model.KDTree;

import java.util.Comparator;

public class POINT_SORT_X implements Comparator<KDTreePoint>{

	@Override
	public int compare(KDTreePoint a, KDTreePoint b) {
		if (a.getX() > b.getX()) return 1;
		if (a.getX() < b.getX()) return -1;
		return 0;
	}
}
