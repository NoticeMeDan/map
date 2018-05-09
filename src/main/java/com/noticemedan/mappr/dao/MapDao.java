package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class MapDao implements DataReader<MapData>, DataWriter<MapData> {
	@Override
	public MapData read(Path input) throws IOException {
		try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(input, StandardOpenOption.READ))) {
			return MapData.class.cast(stream.readObject());
		} catch (ClassNotFoundException e) {
			throw new IOException("An error occurred while reading from .map file.", e);
		}
	}

	@Override
	public MapData write(Path output, MapData data) throws IOException {
		try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(output, StandardOpenOption.CREATE_NEW))) {
			stream.writeObject(data);
			return data;
		}
	}
}
