package com.noticemedan.map.data;

import com.noticemedan.map.App;
import com.noticemedan.map.data.io.OSMReader;
import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.osm.OSMType;
import lombok.Getter;

import java.io.*;
import java.util.*;
import java.util.List;

public class OSMManager extends Observable implements Serializable {
    @Getter
	private List<OSMMaterialElement> osmMaterialElements;

    public OSMManager() {
    	try {
    		File file = new File(App.class.getResource("/bornholm.osm").getFile());
			FileInputStream fileInputStream = new FileInputStream(file);
			OSMReader osmReader = new OSMReader();
			osmMaterialElements = osmReader.getShapesFromFile(fileInputStream);
			System.out.println("Size: " + osmMaterialElements.size());
			dirty();
		}
		catch (IOException e) {
    		e.printStackTrace();
		}
    }

	// TODO @Simon super dirty call to the top layer -> get this away!
    public void dirty() {
    	setChanged();
        notifyObservers();
    }
}
