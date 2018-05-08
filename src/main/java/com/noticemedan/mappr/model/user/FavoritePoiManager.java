package com.noticemedan.mappr.model.user;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class FavoritePoiManager {
	List<FavoritePoi> favoritePOIs;
	@Getter
	ObservableList<FavoritePoi> observableFavoritePOIs;

	public FavoritePoiManager() {
		favoritePOIs = new ArrayList<>();
		observableFavoritePOIs = FXCollections.observableArrayList(favoritePOIs);
	}
}
