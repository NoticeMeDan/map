package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectCreater;
import lombok.Getter;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ForestCreator {
	@Getter Forest forest;
	public ForestCreator() {
		List<MapObject> conList = new LinkedList<>();
		Collection mapObjectsCollection = MapObjectCreater.getInstance(new Dimension(1600,1600)).getMapObjectsByType().values();
		Iterator<List<MapObject>> it = mapObjectsCollection.iterator();
		while (it.hasNext()) conList.addAll(it.next());
		MapObject[] mapObjects = conList.toArray(new MapObject[conList.size()]);
		KDTree tree = new KDTree(mapObjects, 20);
		Forest forest = new Forest();
		forest.setTree(tree);
	}
}
