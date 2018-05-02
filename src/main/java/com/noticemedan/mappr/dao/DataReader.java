package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.TestMapData;

import java.io.IOException;
import java.nio.file.Path;

public interface DataReader {
	TestMapData read(Path input) throws IOException;
}
