package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.data.OsmMapData;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Rect;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;


@Slf4j
public class Forest implements ForestInterface{
	private KDTree trees[];
	private final OsmMapData osmMapData = new OsmMapData();
	private final String binaryID = UUID.randomUUID().toString();
	@Getter
	private List<OsmElement> coastlines = this.osmMapData.getOsmCoastlineElements().toJavaList();

	public Forest() {
		//TODO create different amounts of leafs for zoom levels
		int[] maxNumberOfElementsAtLeaf = new int[] {100, 100, 100, 100, 100};
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
				case PRIMARY:
					zoom0.add(osmElement);
					break;
				case HIGHWAY:
				case SECONDARY:
				case TRUNK:
				case GRASSLAND:
				case HEATH:
				case WATER:
				case SAND:
					zoom1.add(osmElement);
					break;
				case TERTIARY:
				case BUILDING:
				case TREE_ROW:
				case PLAYGROUND:
					zoom2.add(osmElement);
					break;
				case ROAD:
					zoom2.add(osmElement);
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

		this.trees = new KDTree[osmElementArray.length];
		for (int i = 0; i < trees.length; i++) {
			this.trees[i] = new KDTree(osmElementArray[i], maxNumberOfElementsAtLeaf[i]);
		}
		kdTreesToBinary();
	}

	@Override
	public List<OsmElement> rangeSearch(Rect searchQuery, double zoomLevel) {
		ArrayList searchResults = new ArrayList<>();

		if (zoomLevel > 150) searchResults.addAll(trees[2].rangeSearch(searchQuery));
		if (zoomLevel > 0) searchResults.addAll(trees[0].rangeSearch(searchQuery));


		return searchResults;
	}

	//Range search as if only having one zoom level.
	public List<OsmElement> rangeSearch(Rect searchQuery) {
		return rangeSearch(searchQuery, trees.length-1);
	}

	@Override
	public OsmElement nearestNeighbor(double x, double y) {
		throw new RuntimeException("nearestNeighbor() not implemented yet.");
	}

	public void kdTreesToBinary() {
		BinaryMapData.serialize(this.trees, this.binaryID);
	}

	public KDTree[] kdTreesFromBinary() {
		return (KDTree[]) BinaryMapData.deserialize(this.binaryID);
	}
}
