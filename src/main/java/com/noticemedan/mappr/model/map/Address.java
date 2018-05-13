package com.noticemedan.mappr.model.map;

import com.noticemedan.mappr.model.util.Coordinate;
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
	private Coordinate coordinate;

	public String toFullAddress() {
		return String.format(
				"%s %s, %s %s",
				this.street, this.houseNumber, this.city, this.postcode
		);
	}
}
