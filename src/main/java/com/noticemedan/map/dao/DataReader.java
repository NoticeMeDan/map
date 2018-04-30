package com.noticemedan.map.dao;

import java.io.InputStream;

public interface DataReader <T> {
	T read(InputStream inputStream) throws Exception;
}
