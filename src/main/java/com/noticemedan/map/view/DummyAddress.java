package com.noticemedan.map.view;

import lombok.Data;

@Data
public class DummyAddress {
	private String street, house, postcode, city;

	public DummyAddress(String street, String house, String postcode, String city) {
		this.street = street;
		this.house = house;
		this.postcode = postcode;
		this.city = city;
	}

	public String toString() {
		return this.street + " " + this.house;
	}
}
