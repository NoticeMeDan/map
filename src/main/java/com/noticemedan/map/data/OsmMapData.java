package com.noticemedan.map.data;

import com.noticemedan.map.App;
import com.noticemedan.map.data.io.OsmReader;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.OsmElement;
import io.vavr.collection.List;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Observable;

public class OsmMapData implements Serializable {
    @Getter
	private List<OsmElement> osmMaterialElements;
	@Getter
	private List<OsmElement> osmCoastlineElements;

    public OsmMapData() {
    	try {
			File file = new File(App.class.getResource("/bornholm.osm").getFile());
			FileInputStream fileInputStream = new FileInputStream(file);
			OsmReader osmReader = new OsmReader();
			List<List<OsmElement>> elements = osmReader.getShapesFromFile(fileInputStream);
			osmMaterialElements = elements.get(0);
			osmCoastlineElements = elements.get(1);
			System.out.println("Size: " + osmMaterialElements.size());
		}
		catch (IOException e) {
    		e.printStackTrace();
		}
    }
}