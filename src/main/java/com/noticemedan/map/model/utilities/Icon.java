package com.noticemedan.map.model.utilities;

import io.vavr.control.Try;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@NoArgsConstructor
public class Icon {

	public BufferedImage getPointer() {
		return Try.of( () -> ImageIO.read(new File("/home/beta/itu/github/map/src/main/resources/graphics/pointer.png")))
				.getOrNull();
	}

}
