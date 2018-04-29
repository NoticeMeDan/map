package com.noticemedan.map.model.osm;

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

	public String toFullAddress() {
		return String.format(
				"%s %s, %s %s",
				this.street, this.houseNumber, this.city, this.postcode
		);
	}
}
