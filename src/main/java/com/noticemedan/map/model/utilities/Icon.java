package com.noticemedan.map.model.utilities;

import com.noticemedan.map.App;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.osm.Amenity;
import io.vavr.control.Try;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

@Data
public class Icon {
	private HashMap<Amenity,BufferedImage> iconMap;
	private BufferedImage restaurant;
	private BufferedImage pointer;
	private BufferedImage parking;
	private BufferedImage bank;
	private BufferedImage bar;
	private BufferedImage bus;
	private BufferedImage cafe;
	private BufferedImage fast_food;
	private BufferedImage ferry_terminal;
	private BufferedImage fuel;
	private BufferedImage hospital;
	private BufferedImage ice_cream;
	private BufferedImage kindergarten;
	private BufferedImage library;
	private BufferedImage market;
	private BufferedImage police;
	private BufferedImage toilet;
	private BufferedImage university;

	public Icon() {
		this.iconMap = new HashMap();

		this.pointer = readPointer();
		this.restaurant = readRestaurant();
		this.parking = readParking();
		this.bank = readBank();
		this.bar = readBar();
		this.bus = readBus();
		this.cafe = readCafe();
		this.fast_food = readFastFood();
		this.ferry_terminal = readFerry();
		this.fuel = readFuel();
		this.hospital = readHospital();
		this.ice_cream = readIceCream();
		this.kindergarten = readKindergarten();
		this.library = readLibrary();
		this.market = readMarket();
		this.police = readPolice();
		this.toilet = readToilet();
		this.university = readUniversity();

		this.iconMap.put(Amenity.RESTAURANT,this.restaurant);
		this.iconMap.put(Amenity.BANK,this.bank);
		this.iconMap.put(Amenity.BAR,this.bar);
		this.iconMap.put(Amenity.BUS_STATION,this.bus);
		this.iconMap.put(Amenity.CAFE,this.cafe);
		this.iconMap.put(Amenity.COLLEGE,this.university);
		this.iconMap.put(Amenity.FAST_FOOD,this.fast_food);
		this.iconMap.put(Amenity.FERRY_TERMINAL,this.ferry_terminal);
		this.iconMap.put(Amenity.FUEL,this.fuel);
		this.iconMap.put(Amenity.HOSPITAL,this.hospital);
		this.iconMap.put(Amenity.ICE_CREAM,this.ice_cream);
		this.iconMap.put(Amenity.KINDERGARTEN,this.kindergarten);
		this.iconMap.put(Amenity.LIBRARY,this.library);
		this.iconMap.put(Amenity.MARKETPLACE,this.market);
		this.iconMap.put(Amenity.PARKING_ENTRANCE,this.parking);
		this.iconMap.put(Amenity.POLICE,this.police);
		this.iconMap.put(Amenity.PUB,this.bar);
		this.iconMap.put(Amenity.SCHOOL,this.university);
		this.iconMap.put(Amenity.TOILETS,this.toilet);
		this.iconMap.put(Amenity.UNIVERSITY,this.university);
	}

	//resize image of 512x512 to appropriate size and translate it to its shapes position
	public  AffineTransform transform(BufferedImage img, OsmElement e) {
		AffineTransform at = new AffineTransform();
		double size = 0.0000001;
		double width = img.getWidth() * size;
		double height = img.getHeight() * size;
		double xPos = e.getAvgPoint().getX() - width/2;
		double yPos = e.getAvgPoint().getY() - height/2;
		at.translate(xPos,yPos);
		at.scale(size,size);
		return at;
	}

	private BufferedImage readPointer() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/pointer.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readRestaurant() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/restaurant.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readParking() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/parking.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readBank() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/bank.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readBar() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/bar.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readBus() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/bus.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readCafe() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/cafe.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readFastFood() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/fast_food.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readFerry() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/ferry_terminal.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readFuel() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/fuel.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readHospital() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/hospital.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readIceCream() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/ice_cream.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readKindergarten() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/kindergarten.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readLibrary() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/library.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readMarket() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/market.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readPolice() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/police.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readToilet() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/toilet.png").getFile())))
				.getOrNull();
	}

	private BufferedImage readUniversity() {
		return Try.of( () -> ImageIO.read(new File(App.class.getResource("/graphics/university.png").getFile())))
				.getOrNull();
	}
}
