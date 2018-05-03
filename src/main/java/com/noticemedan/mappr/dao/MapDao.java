package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.TestMapData;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class MapDao implements DataReader, DataWriter {
	@Override
	public TestMapData read(Path input) throws IOException {
		// Read data bla bla bla
		return TestMapData.builder().build(); // Magic
	}

	@Override
	public TestMapData write(Path output, TestMapData data) throws IOException {
		// Magic Magic
		return TestMapData.builder().build();
	}
}
