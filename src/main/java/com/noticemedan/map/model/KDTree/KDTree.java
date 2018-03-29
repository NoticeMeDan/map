package com.noticemedan.map.model.KDTree;

import java.util.Arrays;

public class KDTree {

	KDTreeNode rootNode;
	KDTreePoint[] points;
	int N; //Number of nodes to build

	public KDTree(int N) {
		this.N = N;
		buildNodes();
		this.rootNode = buildKDTree(points, 0);
	}

	public static void main(String[] args) {
		KDTree tree = new KDTree(1000);
	}

	private void buildNodes() {
		points = new KDTreePoint[N];

		for(int i = 0; i < N; i++) {
			KDTreePoint point = new KDTreePoint(Math.random(), Math.random());
			points[i] = point;
		}
	}

	//With a single list!
	public KDTreeNode buildKDTree(KDTreePoint[] points, int depth) {
		KDTreePoint[] firstHalfArray;
		KDTreePoint[] secondHalfArray;
		KDTreeNode parent = new KDTreeNode();
		parent.setDepth(depth);

		if (points.length < 10) {
			return new KDTreeNode(points);
		} else if (Utility.isEven(depth)) {
			Arrays.sort(points, new POINT_SORT_X());
			firstHalfArray = Utility.firstHalfArray(points);
			secondHalfArray = Utility.secondHalfArray(points);
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getX());

		} else {
			Arrays.sort(points, new POINT_SORT_Y());
			firstHalfArray = Utility.firstHalfArray(points);
			secondHalfArray = Utility.secondHalfArray(points);
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getY());
		}

		KDTreeNode leftChild = buildKDTree(firstHalfArray, depth+1);
		KDTreeNode rightChild = buildKDTree(secondHalfArray, depth+1);

		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}
}
