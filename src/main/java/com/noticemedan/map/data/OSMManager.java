package com.noticemedan.map.data;

import com.noticemedan.map.App;
import com.noticemedan.map.data.io.OSMReader;
import com.noticemedan.map.model.OSMMaterialElement;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class OSMManager extends Observable implements Serializable {
    @Getter
	private List<OSMMaterialElement> osmMaterialElements;
	@Getter
	private List<OSMMaterialElement> osmCoastlineElements;

    public OSMManager() {
    	try {
			File file = new File(App.class.getResource("/bornholm.osm").getFile());
			FileInputStream fileInputStream = new FileInputStream(file);
			OSMReader osmReader = new OSMReader();
			ArrayList<List<OSMMaterialElement>> elements = osmReader.getShapesFromFile(fileInputStream);
			osmMaterialElements = elements.get(0);
			osmCoastlineElements = elements.get(1);
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
