package com.noticemedan.map.data;

import com.noticemedan.map.App;
import com.noticemedan.map.data.io.OSMReader;
import com.noticemedan.map.model.osm.OSMType;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class OSMManager extends Observable implements Serializable {
    private EnumMap<OSMType, List<Shape>> shapes = initializeMap();

    public OSMManager() {
    	try {
    		File file = new File(App.class.getResource("/bornholm.osm").getFile());
			FileInputStream fileInputStream = new FileInputStream(file);
			OSMReader osmReader = new OSMReader();
			shapes = osmReader.getShapesFromFile(fileInputStream);
			System.out.println(shapes);
			dirty();
		}
		catch (IOException e) {
    		e.printStackTrace();
		}
    }

	private EnumMap<OSMType, List<Shape>> initializeMap() {
		EnumMap<OSMType, List<Shape>> map = new EnumMap<>(OSMType.class);
		for (OSMType type: OSMType.values()) {
			map.put(type, new ArrayList<>());
		}
		return map;
	}

    public void dirty() {
    	setChanged();
        notifyObservers();
    }

    public Iterable<Shape> get(OSMType type) {
        return shapes.get(type);
    }
}
