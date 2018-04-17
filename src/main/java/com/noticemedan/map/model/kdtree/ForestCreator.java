package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.OSMManager;
import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.osm.OSMType;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

public class ForestCreator {
	@Getter
	Forest forest;
	private final OSMManager osmManager = new OSMManager();
	@Getter
	private List<OSMMaterialElement> coastlines = osmManager.getOsmCoastlineElements();

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

		osmMaterialElementArray[0] = zoom0.toArray(new OSMMaterialElement[zoom0.size()]);
		osmMaterialElementArray[1] = zoom1.toArray(new OSMMaterialElement[zoom1.size()]);
		osmMaterialElementArray[2] = zoom2.toArray(new OSMMaterialElement[zoom2.size()]);

		createForest(osmMaterialElementArray, new int[] {100, 100, 100});
	}

	private void createForest(OSMMaterialElement[][] osmMaterialElement, int[] maxNumberOfElementsAtLeaf) {
		if (osmMaterialElement.length != maxNumberOfElementsAtLeaf.length) throw new RuntimeException("Length of parameter arrays are not equal");

		KDTree[] trees = new KDTree[osmMaterialElement.length];

		for (int i = 0; i < trees.length; i++)
			trees[i] = new KDTree(osmMaterialElement[i], maxNumberOfElementsAtLeaf[i]);
		this.forest = new Forest(trees);
	}

	public void addObserver(Observer o) {
		osmManager.addObserver(o);
	}
}
