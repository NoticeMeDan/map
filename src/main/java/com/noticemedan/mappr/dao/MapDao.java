package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import com.noticemedan.mappr.model.util.FileInfo;
import io.vavr.collection.List;
import io.vavr.control.Try;

import java.io.*;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

	public List<FileInfo> getAllFileInfoFromDirectory(Path input) throws IOException {
		NumberFormat formatter = new DecimalFormat("#.0");
		final double SIZE_MB = Math.pow(1024, 2);
		return List.ofAll(Files.walk(input)
							.map(x -> Try.of(() -> FileInfo.builder()
									.name(x.getFileName().toString())
									.date(LocalDateTime.ofInstant(Files.getLastModifiedTime(x).toInstant(), ZoneId.systemDefault()))
									.size(formatter.format(Files.size(x)/SIZE_MB) + "MB")
									.build())))
							.flatMap(x -> x);
	}
}
