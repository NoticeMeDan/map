package com.noticemedan.map.model.KDTree;

import com.noticemedan.map.model.MapObject;
import com.noticemedan.map.model.MapObjectCreater;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ForestCreator {
	@Getter Forest forest;

	public ForestCreator() {
		//List<MapObject>[] mapObjectArrayOfList = MapObjectCreater.getInstance(new Dimension(1600,1600)).getMapObjectsByType().values().toArray();

		Collection mapObjectsCollection = MapObjectCreater.getInstance(new Dimension(1600,1600)).getMapObjectsByType().values();

		List<MapObject> conList = new LinkedList<MapObject>();

		Iterator it = mapObjectsCollection.iterator();
		while (it.hasNext()) {
			conList.addAll(it.next());
		}

		MapObject[] mapObjects = conList.toArray();

		Iterator it = mapObjectsCollection.iterator();
		ArrayList mapObjectsArrayList = new ArrayList<Object>();

		while(it.hasNext()) {
			mapObjectsArrayList.add(it.next());
		}

		/*int mapObjectsSize = 0;
		for (int i = 0; i < mapObjectArrayOfList.length; i++) {
			mapObjectsSize += mapObjectArrayOfList[i].size();
		}

		MapObject[] mapObjects = new MapObject[mapObjectsSize];

		int k = 0;
		for (int i = 0; i < mapObjects.length; i++) {
			for (int j = 0; j < mapObjectArrayOfList[i].size(); j++) {
				mapObjects[k++] = mapObjectArrayOfList[i].get(j);
			}
		}*/

		Forest forest = new Forest();
	}
}
