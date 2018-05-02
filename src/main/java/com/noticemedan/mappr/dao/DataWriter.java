package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.TestMapData;

import java.io.IOException;
import java.nio.file.Path;

public interface DataWriter {
	TestMapData write(Path output, TestMapData data) throws IOException;
}
