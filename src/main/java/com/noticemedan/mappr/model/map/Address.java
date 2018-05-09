package com.noticemedan.mappr.model.map;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Address implements Serializable {
	private String street;
	private String houseNumber;
	private String postcode;
	private String city;
	private String name;
	private double lat;
	private double lon;
	private double onCanvasLat;
	private double onCanvasLon;

	public String toFullAddress() {
		return String.format(
				"%s %s, %s %s",
				this.street, this.houseNumber, this.city, this.postcode
		);
	}
}
