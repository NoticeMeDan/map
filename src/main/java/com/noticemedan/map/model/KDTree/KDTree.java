package com.noticemedan.map.model.KDTree;

import java.util.Arrays;

public class KDTree {

	KDTreeNode rootNode;
	KDTreePoint[] points;
	int N; //Number of nodes to build

	public KDTree(int N) {
		this.N = N;
		buildNodes();

		Stopwatch stopwatch = new Stopwatch();
		this.rootNode = buildKDTree(points, 0);
		System.out.println(stopwatch.elapsedTime());


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

			Stopwatch stopwatch1 = new Stopwatch();
			secondHalfArray = Utility.secondHalfArray(points);
			System.out.println(stopwatch1.elapsedTime());

			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getX());

            /*System.out.println("---------------------------");
            for (Point point : points) {
                System.out.println("x: " + point.getX());
            }
            System.out.println("---------------------------");
            System.out.println("x-splitValue: " + firstHalfArray[firstHalfArray.length-1].getX());
            System.out.println("---------------------------");*/

		} else {
			Stopwatch stopwatch = new Stopwatch();
			Arrays.sort(points, new POINT_SORT_Y());
			System.out.println(stopwatch.elapsedTime());

			firstHalfArray = Utility.firstHalfArray(points);
			secondHalfArray = Utility.secondHalfArray(points);
			parent.setSplitValue(firstHalfArray[firstHalfArray.length-1].getY());

            /*System.out.println("---------------------------");
            for (Point point : points) {
                System.out.println("y: " + point.getY());
            }
            System.out.println("---------------------------");
            System.out.println("y-splitValue: " + firstHalfArray[firstHalfArray.length-1].getY());
            System.out.println("---------------------------");*/
		}

		KDTreeNode leftChild = buildKDTree(firstHalfArray, depth+1);
		KDTreeNode rightChild = buildKDTree(secondHalfArray, depth+1);
		parent.setLeftChild(leftChild);
		parent.setRightChild(rightChild);

		return parent;
	}
}
