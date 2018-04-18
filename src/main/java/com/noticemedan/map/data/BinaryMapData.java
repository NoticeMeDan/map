package com.noticemedan.map.data;

import io.vavr.control.Try;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@NoArgsConstructor
@Slf4j
public class BinaryMapData {
	// TODO: make path relative to where the user saves files
	public static Boolean serialize(Object serializeble, String binaryID) {
		return Try.of(() -> new ObjectOutputStream(new FileOutputStream("./src/main/java/com/noticemedan/map/data/binarydatafiles/" + binaryID + ".dat")))
				.mapTry(x -> writeObject(x, serializeble))
				.map(x -> true)
				.getOrElse(false);
	}

	public static Object deserialize(String binaryID) {
		return Try.of(() -> new ObjectInputStream(new FileInputStream("./src/main/java/com/noticemedan/map/data/binarydatafiles/" + binaryID + ".dat")))
				.mapTry(ObjectInputStream::readObject)
				.getOrNull();
	}

	private static ObjectOutputStream writeObject(ObjectOutputStream outputStream, Object serializeble) throws IOException {
		outputStream.writeObject(serializeble);
		return outputStream;
	}
}
