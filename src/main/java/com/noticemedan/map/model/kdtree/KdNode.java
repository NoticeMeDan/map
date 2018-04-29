package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.model.OsmElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class KdNode {
	private double splitValue;
	private KdNode leftChild;
	private KdNode rightChild;
	private int depth;
	private OsmElement[] elements;
	private String binaryID = UUID.randomUUID().toString();

	public KdNode(OsmElement[] points, int depth) {
		this.elements = points;
		this.depth = depth;
	}

	public void elementsToBinary() {
		BinaryMapData.serialize(this.elements, this.binaryID);
	}

	public OsmElement[] elementsFromBinary() {
		return (OsmElement[]) BinaryMapData.deserialize(this.binaryID);
	}
}
