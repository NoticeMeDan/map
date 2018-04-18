package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.model.OSMMaterialElement;
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
	private OSMMaterialElement[] osmMaterialElements;
	private final String binaryID = UUID.randomUUID().toString();

	public KDTreeNode(OSMMaterialElement[] points, int depth) {
		this.osmMaterialElements = points;
		this.depth = depth;
	}

	public void elementsToBinary() {
		BinaryMapData.serialize(this.osmMaterialElements, this.binaryID);
	}

	public OSMMaterialElement[] elementsFromBinary() {
		return (OSMMaterialElement[]) BinaryMapData.deserialize(this.binaryID);
	}
}
