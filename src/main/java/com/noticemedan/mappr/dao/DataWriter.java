package com.noticemedan.mappr.dao;

import java.io.IOException;
import java.nio.file.Path;

public interface DataWriter <T> {
	T write(Path output, T data) throws IOException;
}
