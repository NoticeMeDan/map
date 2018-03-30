package com.noticemedan.map.model.KDTree;

import lombok.*;

@NoArgsConstructor
public @Data class KDTreeNode {
	double splitValue;
	KDTreeNode leftChild;
	KDTreeNode rightChild;
	int depth;
	KDTreePoint[] points;

	public KDTreeNode(KDTreeNode leftChild, KDTreeNode rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	public KDTreeNode(KDTreePoint[] points, int depth) {
		this.points = points;
		this.depth = depth;
	}
}
