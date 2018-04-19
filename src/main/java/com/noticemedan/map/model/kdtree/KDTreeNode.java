package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryConverter;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.OsmElement;
import lombok.Data;

import java.util.UUID;

@Data
public class KDTreeNode {
	private double splitValue;
	private KDTreeNode leftChild;
	private KDTreeNode rightChild;
	private int depth;
	private OsmElement[] osmElements;
	private String binaryID;

	KDTreeNode() {
		this.binaryID = UUID.randomUUID().toString();
	}

	public KDTreeNode(OsmElement[] points, int depth) {
		this.binaryID = UUID.randomUUID().toString();
		this.osmElements = points;
		this.depth = depth;
	}

	public void elementsToBinary() {
		BinaryConverter bc = new BinaryConverter(this.binaryID);
		bc.serialize(this.osmElements);
	}

	public OsmElement[] elementsFromBinary() {
		BinaryConverter bc = new BinaryConverter(this.binaryID);
		return (OsmElement[]) bc.deserialize();
	}
}
