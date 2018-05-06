package com.noticemedan.map.model.utilities;

import com.noticemedan.map.model.OsmElement;
import io.vavr.control.Try;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

@Data
public class Icon {
	private BufferedImage restaurant;
	private BufferedImage pointer;
	private BufferedImage parking;

	public Icon() {
		this.pointer = readPointer();
		this.restaurant = readRestaurant();
		this.parking = readParking();
	}

	private BufferedImage readPointer() {
		return Try.of( () -> ImageIO.read(new File("/home/beta/itu/github/map/src/main/resources/graphics/pointer.png")))
				.getOrNull();
	}

	private BufferedImage readRestaurant() {
		return Try.of( () -> ImageIO.read(new File("/home/beta/itu/github/map/src/main/resources/graphics/restaurant.png")))
				.getOrNull();
	}

	private BufferedImage readParking() {
		return Try.of( () -> ImageIO.read(new File("/home/beta/itu/github/map/src/main/resources/graphics/parking.png")))
				.getOrNull();
	}

	public  AffineTransform transform(BufferedImage img, OsmElement e) {
		AffineTransform at = new AffineTransform();
		double size = 0.000000068;
		double width = img.getWidth() * size;
		double height = img.getHeight() * size;
		double xPos = e.getAvgPoint().getX() - width/2;
		double yPos = e.getAvgPoint().getY() - height/2;
		at.translate(xPos,yPos);
		at.scale(size,size);
		return at;
	}
}
