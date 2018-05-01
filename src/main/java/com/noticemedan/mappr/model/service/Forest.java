package com.noticemedan.mappr.model.service;

import com.noticemedan.mappr.model.kdtree.ForestInterface;
import com.noticemedan.mappr.model.kdtree.KDTree;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.util.Rect;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Forest implements ForestInterface {
	private KDTree trees[];
	@Getter
	private List<Element> coastlines;

	public Forest(List<Element> elements, List<Element> coastlineElements) {
		//TODO create different amounts of leafs for zoom levels
		int[] maxNumberOfElementsAtLeaf = new int[] {100, 100, 100, 100, 100};
		this.coastlines = coastlineElements;
		Element[][] elementArray = new Element[5][];

		List<Element> zoom0 = new LinkedList<>();
		List<Element> zoom1 = new LinkedList<>();
		List<Element> zoom2 = new LinkedList<>();
		List<Element> zoom3 = new LinkedList<>();
		List<Element> zoom4 = new LinkedList<>();


		elements.forEach(osmElement -> {
			switch (osmElement.getType()) {
				case MOTORWAY:
					zoom0.add(osmElement);
					break;
				case PRIMARY:
					zoom1.add(osmElement);
					break;
				case SECONDARY:
				case TERTIARY:
					zoom2.add(osmElement);
					break;
				case WATER:
				case GRASSLAND:
				case HEATH:
				case PARK:
				case ROAD:
				case FOREST:
					zoom3.add(osmElement);
					break;
				case BUILDING:
				case PLAYGROUND:
					zoom4.add(osmElement);
					break;
				default:
					break;
			}
		});

		elementArray[0] = zoom0.toArray(new Element[0]);
		elementArray[1] = zoom1.toArray(new Element[0]);
		elementArray[2] = zoom2.toArray(new Element[0]);
		elementArray[3] = zoom3.toArray(new Element[0]);
		elementArray[4] = zoom4.toArray(new Element[0]);

		this.trees = new KDTree[elementArray.length];
		for (int i = 0; i < trees.length; i++) {
			this.trees[i] = new KDTree(elementArray[i], maxNumberOfElementsAtLeaf[i]);
		}
	}

	@Override
	public List<Element> rangeSearch(Rect searchQuery, double zoomLevel) {
		ArrayList<Element> searchResults = new ArrayList<>();

		if (zoomLevel > 50) searchResults.addAll(trees[4].rangeSearch(searchQuery));
		if (zoomLevel > 15) searchResults.addAll(trees[3].rangeSearch(searchQuery));
		if (zoomLevel > 1) searchResults.addAll(trees[2].rangeSearch(searchQuery));
		if (zoomLevel > 0.5) searchResults.addAll(trees[1].rangeSearch(searchQuery));
		if (zoomLevel > 0) searchResults.addAll(trees[0].rangeSearch(searchQuery));

		return searchResults;
	}

	//Range search as if only having one zoom level.
	public List<Element> rangeSearch(Rect searchQuery) {
		return rangeSearch(searchQuery, trees.length-1);
	}

	@Override
	public Element nearestNeighbor(double x, double y) {
		throw new RuntimeException("nearestNeighbor() not implemented yet.");
	}
}
