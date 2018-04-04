package com.noticemedan.map.model.KDTree;

import lombok.Data;

public @Data
class KDMapObject implements Comparable<KDMapObject> {
	private double x;
	private double y;
	private boolean depthEven; // Is this object at even depth in KD-Tree?

	public KDMapObject(double x, double y) {
		this.x = x;
		this.y = y;
		this.depthEven = true;
	}

	public int compareTo(KDMapObject that) {
		if(depthEven) {
			if (this.x > that.x) return 1;
			if (this.x == that.x) return 0;
			if (this.x < that.x) return -1;
		} else {
			if (this.y > that.y) return 1;
			if (this.y == that.y) return 0;
			if (this.y < that.y) return -1;
		}
		return 0;
	}
}
