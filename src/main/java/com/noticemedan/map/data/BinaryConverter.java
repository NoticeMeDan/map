package com.noticemedan.map.data;

import com.noticemedan.map.model.OSMMaterialElement;

import java.io.*;

public class BinaryConverter {

	private String filename;

	public BinaryConverter(String binaryID) {
		this.filename = binaryID;
	}

	public void serialize(Object osmElements) {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./binaryData/" + this.filename + ".dat"))) {
			outputStream.writeObject(osmElements);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object deserialize() {
		Object object = null;
		try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("./binaryData/" + this.filename + ".dat"))) {
			object = inputStream.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}
}
