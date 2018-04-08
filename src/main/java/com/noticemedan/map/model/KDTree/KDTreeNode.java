package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import lombok.*;

@NoArgsConstructor
public @Data class KDTreeNode {
	private double splitValue;
	private KDTreeNode leftChild;
	private KDTreeNode rightChild;
	private int depth;
	private MapObject[] points;

	public KDTreeNode(MapObject[] points, int depth) {
		this.points = points;
		this.depth = depth;
	}
}
