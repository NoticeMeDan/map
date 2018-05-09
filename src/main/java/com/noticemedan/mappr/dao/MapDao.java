package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import io.vavr.control.Try;

import java.io.*;
import java.nio.file.Path;

public class  MapDao implements DataReader<MapData>, DataWriter<MapData> {
	@Override
	public MapData read(Path input) throws IOException {
		MapData data;
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(input.toFile()))) {
			data = null;
			try {
				data = (MapData) stream.readObject();
				stream.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	@Override
	public MapData write(Path output, MapData data) throws IOException {
		// Paths.get("hej").toAbsolutePath()
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(output.toFile()));
		writeObject(stream, data);
		stream.close();
		return data;
	}

	private static void writeObject(ObjectOutputStream outputStream, Object serializable) throws IOException {
		outputStream.writeObject(serializable);
	}
}
