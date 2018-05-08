package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;

import java.io.IOException;
import java.nio.file.Path;

public interface DataWriter {
	MapData write(Path output, MapData data) throws IOException;
}
