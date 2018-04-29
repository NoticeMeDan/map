package com.noticemedan.map.model;

import com.noticemedan.map.App;
import com.noticemedan.map.data.OsmReader;
import com.noticemedan.map.model.osm.Address;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class OsmMapData implements Serializable {
    @Getter
	private Vector<OsmElement> osmElements;
	@Getter
	private Vector<OsmElement> osmCoastlineElements;
	@Getter
	private Vector<Address> addresses;

    public OsmMapData() {
    	try {
			String filename = "fyn.osm.zip";
			File file = new File(App.class.getResource("/" + filename).getFile());
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			OsmReader osmReader = new OsmReader();
			Vector<Vector<OsmElement>> elements = osmReader.getShapesFromFile(inputStream, filename);
			osmElements = elements.get(0);
			osmCoastlineElements = elements.get(1);
			addresses = osmReader.getAddresses();
			log.info("Size: " + osmElements.size());
			log.info("Addresses: " + addresses.length());
		}
		catch (IOException e) {
    		e.printStackTrace();
		}
    }
}
