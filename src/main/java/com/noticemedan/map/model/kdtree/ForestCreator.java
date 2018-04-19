package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.OsmMapData;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.osm.OSMType;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

public class ForestCreator {
	@Getter
	Forest forest;
	private final OsmMapData osmMapData = new OsmMapData();
	@Getter
	private List<OsmElement> coastlines = osmMapData.getOsmCoastlineElements().toJavaList(); // TODO: Fix lists

	public ForestCreator() {
		List<OsmElement> osmElements = osmMapData.getOsmMaterialElements().toJavaList();
		OsmElement[][] osmElementArray = new OsmElement[3][];

		List<OsmElement> zoom0 = new LinkedList<>();
		List<OsmElement> zoom1 = new LinkedList<>();
		List<OsmElement> zoom2 = new LinkedList<>();

		osmElements.forEach(m -> {
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

		osmElementArray[0] = zoom0.toArray(new OsmElement[zoom0.size()]);
		osmElementArray[1] = zoom1.toArray(new OsmElement[zoom1.size()]);
		osmElementArray[2] = zoom2.toArray(new OsmElement[zoom2.size()]);

		createForest(osmElementArray, new int[] {100, 100, 100});
	}

	private void createForest(OsmElement[][] osmElement, int[] maxNumberOfElementsAtLeaf) {
		if (osmElement.length != maxNumberOfElementsAtLeaf.length) throw new RuntimeException("Length of parameter arrays are not equal");

		KDTree[] trees = new KDTree[osmElement.length];

		for (int i = 0; i < trees.length; i++)
			trees[i] = new KDTree(osmElement[i], maxNumberOfElementsAtLeaf[i]);
		this.forest = new Forest(trees);
	}
}
