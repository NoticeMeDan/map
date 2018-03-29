package com.noticemedan.map.model.KDTree;

import lombok.*;

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

	public KDTreeNode(KDTreePoint[] points) {
		this.points = points;
	}
	// TODO: How do you avoid this?
	public KDTreeNode() { }
}
