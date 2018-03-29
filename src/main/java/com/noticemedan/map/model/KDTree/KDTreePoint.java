package com.noticemedan.map.model.KDTree;

import lombok.Data;

public @Data
class KDTreePoint {
	double x;
	double y;

	public KDTreePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
}

