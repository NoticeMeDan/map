package com.noticemedan.map.model.user;

import com.noticemedan.map.model.utilities.Coordinate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class User {
	@Getter HashMap<String, Coordinate> favoritePOIs;

	public User() {
		favoritePOIs = new HashMap<>();
	}

	public void addFavoritePOI(Coordinate coordinate, String name) {
		favoritePOIs.put(name, coordinate);
	}
}
