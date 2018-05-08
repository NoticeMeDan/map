package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;

import java.io.*;
import java.nio.file.Path;

public class MapDao implements DataReader<MapData>, DataWriter<MapData> {
	@Override
	public MapData read(Path input) throws IOException {
		// Read data bla bla bla
		return MapData.builder().build(); // Magic
	}

	@Override
	public MapData write(Path output, MapData data) throws IOException {
		// Magic Magic
		return MapData.builder().build();
	}
}
