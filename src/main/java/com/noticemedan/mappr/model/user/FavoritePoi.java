package com.noticemedan.mappr.model.user;

import com.noticemedan.mappr.model.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoritePoi {
	Coordinate coordinate;
	String name;
}
