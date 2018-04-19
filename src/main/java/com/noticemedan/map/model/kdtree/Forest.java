package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.data.OSMManager;
import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.utilities.Rect;
import lombok.Getter;

import java.util.*;

public class Forest {
	private KDTree trees[];
	private final OSMManager osmManager = new OSMManager();
	private final String binaryID = UUID.randomUUID().toString();
	@Getter
	private List<OSMMaterialElement> coastlines = this.osmManager.getOsmCoastlineElements();

	public Forest() {
		int[] maxNumberOfElementsAtLeaf = new int[] {100, 100, 100};
		List<OSMMaterialElement> osmMaterialElements = this.osmManager.getOsmMaterialElements();
		OSMMaterialElement[][] osmMaterialElementArray = new OSMMaterialElement[3][];

		List<OSMMaterialElement> zoom0 = new LinkedList<>();
		List<OSMMaterialElement> zoom1 = new LinkedList<>();
		List<OSMMaterialElement> zoom2 = new LinkedList<>();

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

		osmMaterialElementArray[0] = zoom0.toArray(new OSMMaterialElement[0]);
		osmMaterialElementArray[1] = zoom1.toArray(new OSMMaterialElement[0]);
		osmMaterialElementArray[2] = zoom2.toArray(new OSMMaterialElement[0]);

		this.trees = new KDTree[osmMaterialElementArray.length];

		for (int i = 0; i < trees.length; i++) {
			this.trees[i] = new KDTree(osmMaterialElementArray[i], maxNumberOfElementsAtLeaf[i]);
		}

		kdTreesToBinary();
	}

	//@Override
	public List<OSMMaterialElement> rangeSearch(Rect searchQuery, int zoomLevel) {
		ArrayList searchResults = new ArrayList<>();
		for (int i = 0; i < zoomLevel+1; i++) {
			searchResults.addAll(trees[i].rangeSearch(searchQuery));
		}
		return searchResults;
	}

	//Range search as if only having one zoom level.
	public List<OSMMaterialElement> rangeSearch(Rect searchQuery) {
		return rangeSearch(searchQuery, trees.length-1);
	}

	//@Override
	public OSMMaterialElement nearestNeighbor(double x, double y) {
		throw new RuntimeException("nearestNeighbor() not implemented yet.");
	}

	public void kdTreesToBinary() {
		BinaryMapData.serialize(this.trees, this.binaryID);
	}

	public KDTree[] kdTreesFromBinary() {
		return (KDTree[]) BinaryMapData.deserialize(this.binaryID);
	}

	public void addObserver(Observer o) {
		osmManager.addObserver(o);
	}
}
