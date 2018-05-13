package com.noticemedan.mappr.model.user;

import com.noticemedan.mappr.model.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FavoritePoi implements Serializable {
	Coordinate coordinate;
	String name;
}
