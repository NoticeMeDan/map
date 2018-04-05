package com.noticemedan.map.model.KDTree;

import lombok.*;

@NoArgsConstructor
public @Data class KDTreeNode {
	private double splitValue;
	private KDTreeNode leftChild;
	private KDTreeNode rightChild;
	private int depth;
	private KDMapObject[] points;

	public KDTreeNode(KDMapObject[] points, int depth) {
		this.points = points;
		this.depth = depth;
	}
}
