package com.noticemedan.map.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
	private String street;
	private String houseNumber;
	private String postcode;
	private String city;
	private String name;
	private double lat;
	private double lon;
	private double onCanvasLat;
	private double onCanvasLon;

	public String streetAndHouseToString() {
		return this.street + " " + this.houseNumber;
	}
}
