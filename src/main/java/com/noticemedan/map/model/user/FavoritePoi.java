package com.noticemedan.map.model.user;

import com.noticemedan.map.model.utilities.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoritePoi {
	Coordinate coordinate;
	String name;
}
