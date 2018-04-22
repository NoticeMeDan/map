package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.data.OsmMapData;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.utilities.Rect;
import lombok.Getter;

import java.util.*;



public class Forest implements ForestInterface{
	private KDTree trees[];
	private final OsmMapData osmMapData = new OsmMapData();
	private final String binaryID = UUID.randomUUID().toString();
	@Getter
	private List<OsmElement> coastlines = this.osmMapData.getOsmCoastlineElements().toJavaList();

	public Forest() {
		//TODO create different amounts of leafs for zoom levels
		int[] maxNumberOfElementsAtLeaf = new int[] {100, 100, 100};
		List<OsmElement> osmMaterialElements = this.osmMapData.getOsmElements().toJavaList();
		OsmElement[][] osmMaterialElementArray = new OsmElement[3][];

		List<OsmElement> zoom0 = new LinkedList<>();
		List<OsmElement> zoom1 = new LinkedList<>();
		List<OsmElement> zoom2 = new LinkedList<>();

		osmMaterialElements.forEach(m -> {
			switch (m.getOsmType()) {
				case COASTLINE:
				case UNKNOWN:
				case MOTORWAY:
				case TRUNK:
				case GRASSLAND:
				case HEATH:
				case WATER:
				case SAND:
					zoom0.add(m);

				case HIGHWAY:
				case SECONDARY:
					zoom1.add(m);

				case TERTIARY:
				case BUILDING:
				case TREE_ROW:
				case PLAYGROUND:
				case ROAD:
					zoom2.add(m);

				default:
					break;
			}
		});

		osmMaterialElementArray[0] = zoom0.toArray(new OsmElement[0]);
		osmMaterialElementArray[1] = zoom1.toArray(new OsmElement[0]);
		osmMaterialElementArray[2] = zoom2.toArray(new OsmElement[0]);

		this.trees = new KDTree[osmMaterialElementArray.length];

		for (int i = 0; i < trees.length; i++) {
			this.trees[i] = new KDTree(osmMaterialElementArray[i], maxNumberOfElementsAtLeaf);
		}

		kdTreesToBinary();
	}

	@Override
	public List<OsmElement> rangeSearch(Rect searchQuery, int zoomLevel) {
		ArrayList searchResults = new ArrayList<>();
		for (int i = 0; i < zoomLevel+1; i++) {
			searchResults.addAll(trees[i].rangeSearch(searchQuery));
		}
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
