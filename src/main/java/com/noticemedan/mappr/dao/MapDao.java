package com.noticemedan.mappr.dao;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class MapDao implements DataReader<byte[]>, DataWriter<byte[]> {
	@Override
	public byte[] read(InputStream inputStream) throws Exception {
		return new ObjectInputStream(inputStream).readAllBytes();
	}

	@Override
	public OutputStream write(OutputStream outputStream, byte[] data) throws Exception {
		ObjectOutputStream writer = new ObjectOutputStream(outputStream);
		writer.writeObject(data);
		return writer;
	}
}
