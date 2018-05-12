package com.noticemedan.mappr.model.user;

import com.noticemedan.mappr.model.map.Element;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class FavoritePoiManager {
	List<Element> favoritePois;
	@Getter
	ObservableList<Element> observableFavoritePois;

	public FavoritePoiManager() {
		favoritePois = new ArrayList<>();
		observableFavoritePois = FXCollections.observableArrayList(favoritePois);
	}
}
