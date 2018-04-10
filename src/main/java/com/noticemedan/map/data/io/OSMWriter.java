package com.noticemedan.map.data.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class OSMWriter {

	public void save(FileOutputStream fileOutputStream) {
		try {
			ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
			/*os.writeObject(shapes);
			os.writeObject(minLon);
			os.writeObject(minLat);
			os.writeObject(maxLon);
			os.writeObject(maxLat); */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
