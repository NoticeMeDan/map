package com.noticemedan.mappr.model.map;

import com.noticemedan.mappr.model.util.Coordinate;
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
	private Coordinate coordinate;

	public String toFullAddress() {
		return String.format(
				"%s %s, %s %s",
				this.street, this.houseNumber, this.city, this.postcode
		);
	}
}
