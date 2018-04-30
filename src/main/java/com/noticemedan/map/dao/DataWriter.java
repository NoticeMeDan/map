package com.noticemedan.map.dao;

import java.io.OutputStream;

public interface DataWriter <T> {
	OutputStream write(OutputStream outputStream, T data) throws Exception;
}
