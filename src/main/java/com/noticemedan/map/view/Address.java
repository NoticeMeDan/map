package com.noticemedan.map.view;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class Address {
	@Getter @Setter
	private String street;
	@Getter @Setter
	private String houseNumber;
	@Getter @Setter
	private String postcode;
	@Getter @Setter
	private String city;
	@Getter @Setter
	private String name;
	@Getter @Setter
	private double lat;
	@Getter @Setter
	private double lon;
	@Getter @Setter
	private double onCanvasLat;
	@Getter @Setter
	private double onCanvasLon;

	public String streetAndHouseToString() {
		return this.street + " " + this.houseNumber;
	}
}
