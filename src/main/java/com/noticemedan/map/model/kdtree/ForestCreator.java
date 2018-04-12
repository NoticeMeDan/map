package com.noticemedan.map.model.kdtree;

import com.noticemedan.map.data.OSMManager;
import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.utilities.Rect;
import lombok.Getter;

import java.util.*;

//TODO: Delete all these collections when enummap has been removed.

public class ForestCreator {
	@Getter
	Forest forest;
	private final OSMManager osmManager = new OSMManager();

	/**
	 * This is a short term solution of range search with zoom levels that uses the enummap.
	 * TODO: Delete this constructor after deletion of enummap.
	 */
	/*public ForestCreator() {
		List<OSMMaterialElement> conList = new LinkedList<>();
		Collection osmElementCollection = osmManager.getOsmMaterialElements();

		Iterator<List<OSMMaterialElement>> it = osmElementCollection.iterator();
		while (it.hasNext()) conList.addAll(it.next());
		OSMMaterialElement[] osmMaterialElement = conList.toArray(new OSMMaterialElement[conList.size()]);

		//This is the activity that mapcreator is supposed to do.
		ArrayList<OSMMaterialElement>[] zoomLevels = new ArrayList[3];
		for (int i = 0; i < zoomLevels.length; i++) zoomLevels[i] = new ArrayList<>();
		for (int i = 0; i < osmMaterialElement.length; i++) {

			if (osmMaterialElement[i].getOsmType() == COASTLINE) zoomLevels[0].add(osmMaterialElement[i]);
			if (osmMaterialElement[i].getOsmType() == HIGHWAY) zoomLevels[0].add(osmMaterialElement[i]);
			if (osmMaterialElement[i].getOsmType() == GRASSLAND) zoomLevels[0].add(osmMaterialElement[i]);
			if (osmMaterialElement[i].getOsmType() == WATER) zoomLevels[0].add(osmMaterialElement[i]);

			if (osmMaterialElement[i].getOsmType() == ROAD) zoomLevels[1].add(osmMaterialElement[i]);
			if (osmMaterialElement[i].getOsmType() == TREE_ROW) zoomLevels[1].add(osmMaterialElement[i]);

			if (osmMaterialElement[i].getOsmType() == BUILDING) zoomLevels[2].add(osmMaterialElement[i]);
		}
		KDTree[] trees = new KDTree[3];
		for (int i = 0; i < trees.length; i++)
			trees[i] = new KDTree(zoomLevels[i].toArray(new OSMMaterialElement[zoomLevels[i].size()]), 20);
		this.forest = new Forest(trees);
	}*/

	public ForestCreator() {
		List<OSMMaterialElement> osmMaterialElements = osmManager.getOsmMaterialElements();
		OSMMaterialElement[][] osmMaterialElementArray = new OSMMaterialElement[1][];
		// TODO Clean this mess up
		osmMaterialElementArray[0] = osmMaterialElements.toArray(new OSMMaterialElement[osmMaterialElements.size()]);
		createForest(osmMaterialElementArray, new int[] {100});
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
