package com.noticemedan.mappr.dao;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageDao implements DataReader<BufferedImage> {

	@Override
	public BufferedImage read(Path input) throws IOException {
		return ImageIO.read(Files.newInputStream(input));
	}
}
