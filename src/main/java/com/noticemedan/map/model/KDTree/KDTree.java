package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;

public class KDTree implements Tree {

	@Getter private KDTreeNode rootNode;
	private int maxNumberOfElementsAtLeaf;

	public KDTree(KDTreePoint[] points, int maxNumberOfElementsAtLeaf) {
		if (points.length == 0) throw new RuntimeException("Length of passed array to KD Tree is 0");
		if (maxNumberOfElementsAtLeaf < 1 ) throw new RuntimeException("The maximum number of elements at a leaf cannot be less than 1");

		this.maxNumberOfElementsAtLeaf = maxNumberOfElementsAtLeaf;
		this.rootNode = buildKDTree(points, 0);
	}

	/**
	 * @return 		Root node of KDTree.
	 */
	private KDTreeNode buildKDTree(KDTreePoint[] points, int depth) {
		KDTreePoint[] firstHalfArray;
		KDTreePoint[] secondHalfArray;
		KDTreeNode parent = new KDTreeNode();
		parent.setDepth(depth);

		// Define size of points array in leaf nodes.
		if (points.length <= maxNumberOfElementsAtLeaf ) {
			return new KDTreeNode(points, depth);
		} else if (depth % 2 == 0) { // If depth even, split by x-value
			Tuple2<KDTreePoint[], KDTreePoint[]> tuple2 = splitPointArrayByMedian(points);
			firstHalfArray = tuple2._1;
			secondHalfArray = tuple2._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getX());
		} else { // If depth odd, split by y-value
			Tuple2<KDTreePoint[], KDTreePoint[]> tuple2 = splitPointArrayByMedian(points);
			firstHalfArray = tuple2._1;
			secondHalfArray = tuple2._2;
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getY());
		}

		// Recursively find the parent's left and right child.
		KDTreeNode leftChild = buildKDTree(firstHalfArray, depth+1);
		KDTreeNode rightChild = buildKDTree(secondHalfArray, depth+1);

		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}

	private Tuple2<KDTreePoint[], KDTreePoint[]> splitPointArrayByMedian(KDTreePoint[] points) {
		int N = points.length;

		// Handle small array cases:
		if (N == 0) throw new RuntimeException("Zero element array passed as parameter.");
		if (N == 1) throw new RuntimeException("One element array cannot be split further.");
		if (N == 2) return Tuple.of(new KDTreePoint[] { points[0] }, new KDTreePoint[] { points[1] } );

		Quick.select(points, N/2);

		// k is the index where the array should be split.
		int k = N/2+1;
		KDTreePoint[] 	firstHalf = new KDTreePoint[k];
		KDTreePoint[]   secondHalf = new KDTreePoint[N-k];

		// Insert elements into two arrays from original array.
		int j = 0;
		for (int i = 0; i < N; i++) {
			if (points[i].getSortX()) {	points[i].setSortX(false); }
			else {						points[i].setSortX(true);  }
			if (i < k) 						firstHalf[i] = points[i];
			if (i >= k) 					secondHalf[j++] = points[i];
		}
		return Tuple.of(firstHalf, secondHalf);
	}


	@Override
	public ArrayList<MapObject> rangeSearch(Point SW, Point NE) {
		throw new RuntimeException("Range search not implemented yet.");
	}

	@Override
	public MapObject nearestNeighbor(Point queryPoint) {
		throw new RuntimeException("Nearest neighbor search not implemented yet");
	}
}
