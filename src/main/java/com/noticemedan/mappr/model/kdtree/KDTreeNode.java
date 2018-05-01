package com.noticemedan.mappr.model.kdtree;

import com.noticemedan.mappr.model.osm.Element;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class KDTreeNode {
	private double splitValue;
	private KDTreeNode leftChild;
	private KDTreeNode rightChild;
	private int depth;
	private Element[] elements;
	private String binaryID = UUID.randomUUID().toString();

	public KDTreeNode(Element[] points, int depth) {
		this.elements = points;
		this.depth = depth;
	}
}
