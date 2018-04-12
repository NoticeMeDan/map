package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryConverter;
import com.noticemedan.map.model.OSMMaterialElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class KDTreeNode {
	private double splitValue;
	private KDTreeNode leftChild;
	private KDTreeNode rightChild;
	private int depth;
	private OSMMaterialElement[] osmMaterialElements;
	private String binaryID;

	KDTreeNode() {
		this.binaryID = UUID.randomUUID().toString();
	}

	public KDTreeNode(OSMMaterialElement[] points, int depth) {
		this.binaryID = UUID.randomUUID().toString();
		this.osmMaterialElements = points;
		this.depth = depth;
	}

	public void elementsToBinary() {
		BinaryConverter bc = new BinaryConverter(this.binaryID);
		bc.serialize(this.osmMaterialElements);
	}

	public OSMMaterialElement[] elementsFromBinary() {
		BinaryConverter bc = new BinaryConverter(this.binaryID);
		return (OSMMaterialElement[]) bc.deserialize();
	}
}
