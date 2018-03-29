package com.noticemedan.map.model.KDTree;

import lombok.Data;

public @Data
class KDTreePoint implements Comparable<KDTreePoint> {
	double x;
	double y;
	boolean sortX = true;

	public KDTreePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public int compareTo(KDTreePoint that) {
		if(sortX) {
			if (this.x > that.x) return 1;
			if (this.x == that.y) return 0;
			if (this.x < that.y) return -1;
		} else {
			if (this.y > that.y) return 1;
			if (this.y == that.y) return 0;
			if (this.y < that.y) return -1;
		}
		//TODO This will never run right?
		return 0;
	}
}
