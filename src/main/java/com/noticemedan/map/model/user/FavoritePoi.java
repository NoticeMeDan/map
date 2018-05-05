package com.noticemedan.map.model.user;

import com.noticemedan.map.model.utilities.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class FavoritePoi {
	@Getter
	Coordinate coordinate;
	String name;
}
