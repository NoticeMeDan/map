package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import com.noticemedan.mappr.model.map.FileInfo;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MapDao implements DataReader<MapData>, DataWriter<MapData> {
	@Override
	public MapData read(Path input) throws IOException {
		try (ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(input, StandardOpenOption.READ)))) {
			return MapData.class.cast(stream.readObject());
		} catch (ClassNotFoundException e) {
			throw new IOException("An error occurred while reading from .map file.", e);
		}
	}

	@Override
	public MapData write(Path output, MapData data) throws IOException {
		if (output.getParent() != null) Files.createDirectories(output.getParent());
		try (ObjectOutputStream stream = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(output,
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)))) {
			stream.writeObject(data);
			return data;
		}
	}

	public Path delete(Path file) throws IOException {
		Files.deleteIfExists(file);
		return file;
	}

	public List<FileInfo> getAllFileInfoFromDirectory(Path dir) throws IOException {
		if (!Files.isDirectory(dir)) return List.empty();
		return List.ofAll(Files.walk(dir)
							.map(x -> Try.of(() -> FileInfo.builder()
									.fileName(x.getFileName().toString())
									.displayName(x.getFileName().toString().split("__")[0])
									.lastEdited(LocalDateTime.ofInstant(Files.getLastModifiedTime(x).toInstant(), ZoneId.systemDefault()))
									.size(Files.size(x))
									.build())))
							.flatMap(x -> x)
							.drop(1); // First element is the directory itself
	}
}
