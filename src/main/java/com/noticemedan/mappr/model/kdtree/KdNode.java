package com.noticemedan.mappr.model.kdtree;

import com.noticemedan.mappr.model.map.Element;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class KdNode {
	private double splitValue;
	private Element splitElement;
	private KdNode leftChild;
	private KdNode rightChild;
	private int depth;
	private Element[] elements;
	private String binaryID = UUID.randomUUID().toString();

	public KdNode(Element[] points, int depth) {
		this.elements = points;
		this.depth = depth;
	}
}
