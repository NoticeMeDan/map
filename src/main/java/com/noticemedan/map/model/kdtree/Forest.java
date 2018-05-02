package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.data.OsmMapData;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Coordinate;
import com.noticemedan.map.model.utilities.Rect;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
public class Forest implements ForestInterface{
	private KdTree trees[];
	private final OsmMapData osmMapData = new OsmMapData();
	private final String binaryID = UUID.randomUUID().toString();
	@Getter
	private List<OsmElement> coastlines = this.osmMapData.getOsmCoastlineElements().toJavaList();

	public Forest() {
		//TODO create different amounts of leafs for zoom levels
		int[] maxNumberOfElementsAtLeaf = new int[] {1000, 1000, 1000, 1000, 1000};
		List<OsmElement> osmElements = this.osmMapData.getOsmElements().toJavaList();
		OsmElement[][] osmElementArray = new OsmElement[5][];

		List<OsmElement> zoom0 = new LinkedList<>();
		List<OsmElement> zoom1 = new LinkedList<>();
		List<OsmElement> zoom2 = new LinkedList<>();
		List<OsmElement> zoom3 = new LinkedList<>();
		List<OsmElement> zoom4 = new LinkedList<>();


		osmElements.forEach(osmElement -> {
			switch (osmElement.getOsmType()) {
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

		osmElementArray[0] = zoom0.toArray(new OsmElement[0]);
		osmElementArray[1] = zoom1.toArray(new OsmElement[0]);
		osmElementArray[2] = zoom2.toArray(new OsmElement[0]);
		osmElementArray[3] = zoom3.toArray(new OsmElement[0]);
		osmElementArray[4] = zoom4.toArray(new OsmElement[0]);

		this.trees = new KdTree[osmElementArray.length];
		for (int i = 0; i < trees.length; i++) {
			this.trees[i] = new KdTree(osmElementArray[i], maxNumberOfElementsAtLeaf[i]);
		}
		kdTreesToBinary();
	}

	@Override
	public List<OsmElement> rangeSearch(Rect searchQuery, double zoomLevel) {
		ArrayList<OsmElement> searchResults = new ArrayList<>();

		if (zoomLevel > 50) searchResults.addAll(trees[4].rangeSearch(searchQuery));
		if (zoomLevel > 15) searchResults.addAll(trees[3].rangeSearch(searchQuery));
		if (zoomLevel > 1) searchResults.addAll(trees[2].rangeSearch(searchQuery));
		if (zoomLevel > 0.5) searchResults.addAll(trees[1].rangeSearch(searchQuery));
		if (zoomLevel > 0) searchResults.addAll(trees[0].rangeSearch(searchQuery));

		return searchResults;
	}

	@Override
	public OsmElement nearestNeighbor(Coordinate queryPoint, double zoomLevel) {
		int excludeTrees = 0;

		if (zoomLevel > 0) excludeTrees = 4;
		if (zoomLevel > 0.5) excludeTrees = 3;
		if (zoomLevel > 1) excludeTrees = 2;
		if (zoomLevel > 15) excludeTrees = 1;
		if (zoomLevel > 50) excludeTrees = 0;

		OsmElement currentNN = new OsmElement();
		currentNN.setAvgPoint(new Coordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
		for (int i = 0; i < trees.length-excludeTrees; i++) {
			OsmElement candidate = trees[i].nearestNeighbor(queryPoint);
			double distanceCurrentNNToQueryPoint = Coordinate.euclidianDistance(currentNN.getAvgPoint(), queryPoint);
			double distanceCandidateToQueryPoint = Coordinate.euclidianDistance(candidate.getAvgPoint(), queryPoint);
			if (distanceCandidateToQueryPoint < distanceCurrentNNToQueryPoint) currentNN = candidate;
		}
		return currentNN;
	}

	public void kdTreesToBinary() {
		BinaryMapData.serialize(this.trees, this.binaryID);
	}

	public KdTree[] kdTreesFromBinary() {
		return (KdTree[]) BinaryMapData.deserialize(this.binaryID);
	}
}
