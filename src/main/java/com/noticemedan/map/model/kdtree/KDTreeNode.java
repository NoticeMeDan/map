package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.model.OsmElement;
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
	private OsmElement[] osmElements;
	private String binaryID = UUID.randomUUID().toString();

	public KDTreeNode(OsmElement[] points, int depth) {
		this.osmElements = points;
		this.depth = depth;
	}

	public void elementsToBinary() {
		BinaryMapData.serialize(this.osmElements, this.binaryID);
	}

	public OsmElement[] elementsFromBinary() {
		return (OsmElement[]) BinaryMapData.deserialize(this.binaryID);
	}
}
