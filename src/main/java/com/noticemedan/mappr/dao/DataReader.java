package com.noticemedan.mappr.dao;

import java.io.IOException;
import java.nio.file.Path;

public interface DataReader <T> {
	T read(Path input) throws IOException;
}
