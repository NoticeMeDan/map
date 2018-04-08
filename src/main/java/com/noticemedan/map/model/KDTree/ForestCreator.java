package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectCreater;
import lombok.Getter;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.noticemedan.map.model.OSMType.*;

public class ForestCreator {
	@Getter Forest forest;

	/**
	 * TODO: This is a short term solution of range search with zoom levels that uses the enummap.
	 * TODO: Delete this constructor after deletion of enummap.
	 */
	public ForestCreator() {
		//TODO: Delete this section when MapCreater can transfer objects directly to this class.
		List<MapObject> conList = new LinkedList<>();
		Collection mapObjectsCollection = MapObjectCreater.getInstance(new Dimension(1600,1600)).getMapObjectsByType().values();
		Iterator<List<MapObject>> it = mapObjectsCollection.iterator();
		while (it.hasNext()) conList.addAll(it.next());
		MapObject[] mapObjects = conList.toArray(new MapObject[conList.size()]);

		//TODO: Delete this section when MapCreater can transfer objects directly to this class.
		//This is the activity that mapcreator is supposed to do.
		ArrayList<MapObject>[] zoomLevels = new ArrayList[3];
		for (int i = 0; i < zoomLevels.length; i++) zoomLevels[i] = new ArrayList<>();
		for (int i = 0; i < mapObjects.length; i++) {
			if (mapObjects[i].getOsmType() == HIGHWAY) zoomLevels[0].add(mapObjects[i]);
			if (mapObjects[i].getOsmType() == GRASSLAND) zoomLevels[0].add(mapObjects[i]);
			if (mapObjects[i].getOsmType() == WATER) zoomLevels[0].add(mapObjects[i]);

			if (mapObjects[i].getOsmType() == ROAD) zoomLevels[1].add(mapObjects[i]);
			if (mapObjects[i].getOsmType() == TREE_ROW) zoomLevels[1].add(mapObjects[i]);

			if (mapObjects[i].getOsmType() == BUILDING) zoomLevels[2].add(mapObjects[i]);
		}
		KDTree[] trees = new KDTree[3];
		for (int i = 0; i < trees.length; i++) trees[i] = new KDTree(zoomLevels[i].toArray(new MapObject[zoomLevels[i].size()]), 20);
		this.forest = new Forest(trees);
	}

	public ForestCreator(MapObject[][] mapObjects, int[] maxNumberOfElementsAtLeaf) {
		if (mapObjects.length != maxNumberOfElementsAtLeaf.length) throw new RuntimeException("Length of parameter arrays are not equal");

		KDTree[] trees = new KDTree[mapObjects.length];
		for (int i = 0; i < trees.length; i++) trees[i] = new KDTree(mapObjects[i], maxNumberOfElementsAtLeaf[i]);
		this.forest = new Forest(trees);
	}
}
