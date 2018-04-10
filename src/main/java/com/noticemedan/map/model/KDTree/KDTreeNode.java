package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.OSMMaterialElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public @Data class KDTreeNode {
	private double splitValue;
	private KDTreeNode leftChild;
	private KDTreeNode rightChild;
	private int depth;
	private OSMMaterialElement[] points;

	public KDTreeNode(OSMMaterialElement[] points, int depth) {
		this.points = points;
		this.depth = depth;
	}
}
