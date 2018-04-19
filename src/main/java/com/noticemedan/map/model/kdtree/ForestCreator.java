package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.BinaryMapData;
import com.noticemedan.map.data.OSMManager;
import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.osm.OSMType;
import com.noticemedan.map.model.utilities.Rect;
import lombok.Getter;

import java.util.*;

public class ForestCreator {
	private final OSMManager osmManager = new OSMManager();
	@Getter
	private List<OSMMaterialElement> coastlines = osmManager.getOsmCoastlineElements();
	private KDTree trees[];
	private final String binaryID = UUID.randomUUID().toString();

	public ForestCreator() {
		List<OSMMaterialElement> osmMaterialElements = osmManager.getOsmMaterialElements();
		OSMMaterialElement[][] osmMaterialElementArray = new OSMMaterialElement[3][];

		List<OSMMaterialElement> zoom0 = new LinkedList<>();
		List<OSMMaterialElement> zoom1 = new LinkedList<>();
		List<OSMMaterialElement> zoom2 = new LinkedList<>();

		osmMaterialElements.forEach(m -> {
			if (m.getOsmType().equals(OSMType.COASTLINE)) zoom0.add(m);
			if (m.getOsmType().equals(OSMType.UNKNOWN)) zoom0.add(m);
			if (m.getOsmType().equals(OSMType.MOTORWAY)) zoom0.add(m);
			if (m.getOsmType().equals(OSMType.TRUNK)) zoom0.add(m);
			if (m.getOsmType().equals(OSMType.GRASSLAND)) zoom0.add(m);
			if (m.getOsmType().equals(OSMType.HEATH)) zoom0.add(m);
			if (m.getOsmType().equals(OSMType.WATER)) zoom0.add(m);
			if (m.getOsmType().equals(OSMType.SAND)) zoom0.add(m);

			if (m.getOsmType().equals(OSMType.HIGHWAY)) zoom1.add(m);
			if (m.getOsmType().equals(OSMType.SECONDARY)) zoom1.add(m);

			if (m.getOsmType().equals(OSMType.TERTIARY)) zoom2.add(m);
			if (m.getOsmType().equals(OSMType.BUILDING)) zoom2.add(m);
			if (m.getOsmType().equals(OSMType.TREE_ROW)) zoom2.add(m);
			if (m.getOsmType().equals(OSMType.PLAYGROUND)) zoom2.add(m);
			if (m.getOsmType().equals(OSMType.ROAD)) zoom2.add(m);
		});

		osmMaterialElementArray[0] = zoom0.toArray(new OSMMaterialElement[0]);
		osmMaterialElementArray[1] = zoom1.toArray(new OSMMaterialElement[0]);
		osmMaterialElementArray[2] = zoom2.toArray(new OSMMaterialElement[0]);

		createForest(osmMaterialElementArray, new int[] {100, 100, 100});
	}

	private void createForest(OSMMaterialElement[][] osmMaterialElement, int[] maxNumberOfElementsAtLeaf) {
		if (osmMaterialElement.length != maxNumberOfElementsAtLeaf.length) throw new RuntimeException("Length of parameter arrays are not equal");

		this.trees = new KDTree[osmMaterialElement.length];

		for (int i = 0; i < trees.length; i++) {
			this.trees[i] = new KDTree(osmMaterialElement[i], maxNumberOfElementsAtLeaf[i]);
		}
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

	public void kdTreeToBinary() {
		BinaryMapData.serialize(this.trees, this.binaryID);
	}

	public KDTree[] kdTreesFromBinary() {
		return (KDTree[]) BinaryMapData.deserialize(this.binaryID);
	}

	public void addObserver(Observer o) {
		osmManager.addObserver(o);
	}
}
