package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.OSMElementCreator;
import com.noticemedan.map.model.OSMMaterialElement;
import lombok.Getter;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.noticemedan.map.model.OSMType.*;

//TODO: Delete all these collections when enummap has been removed.

public class ForestCreator {
	@Getter Forest forest;

	/**
	 * This is a short term solution of range search with zoom levels that uses the enummap.
	 * TODO: Delete this constructor after deletion of enummap.
	 */
	public ForestCreator() {
		List<OSMMaterialElement> conList = new LinkedList<>();
		Collection mapObjectsCollection = OSMElementCreator.getInstance(new Dimension(1600, 1600)).getMapObjectsByType().values();
		Iterator<List<OSMMaterialElement>> it = mapObjectsCollection.iterator();
		while (it.hasNext()) conList.addAll(it.next());
		OSMMaterialElement[] osmMaterialElement = conList.toArray(new OSMMaterialElement[conList.size()]);

		//This is the activity that mapcreator is supposed to do.
		ArrayList<OSMMaterialElement>[] zoomLevels = new ArrayList[3];
		for (int i = 0; i < zoomLevels.length; i++) zoomLevels[i] = new ArrayList<>();
		for (int i = 0; i < osmMaterialElement.length; i++) {
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
	}

	public ForestCreator(OSMMaterialElement[][] osmMaterialElement, int[] maxNumberOfElementsAtLeaf) {
		if (osmMaterialElement.length != maxNumberOfElementsAtLeaf.length)
			throw new RuntimeException("Length of parameter arrays are not equal");

		KDTree[] trees = new KDTree[osmMaterialElement.length];
		for (int i = 0; i < trees.length; i++)
			trees[i] = new KDTree(osmMaterialElement[i], maxNumberOfElementsAtLeaf[i]);
		this.forest = new Forest(trees);
	}
}
