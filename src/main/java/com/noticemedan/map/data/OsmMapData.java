package com.noticemedan.map.data;

import com.noticemedan.map.App;
import com.noticemedan.map.data.io.OsmReader;
import com.noticemedan.map.model.OsmElement;
import io.vavr.collection.Vector;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

public class OsmMapData implements Serializable {
    @Getter
	private Vector<OsmElement> osmElements;
	@Getter
	private Vector<OsmElement> osmCoastlineElements;

    public OsmMapData() {
    	try {
			String filename = "denmark-latest.osm";
			File file = new File(App.class.getResource("/" + filename).getFile());
			FileInputStream fileInputStream = new FileInputStream(file);
			OsmReader osmReader = new OsmReader();
			Vector<Vector<OsmElement>> elements = osmReader.getShapesFromFile(fileInputStream, filename);
			osmElements = elements.get(0);
			osmCoastlineElements = elements.get(1);
			System.out.println("Size: " + osmElements.size());
		}
		catch (IOException e) {
    		e.printStackTrace();
		}
    }
}
