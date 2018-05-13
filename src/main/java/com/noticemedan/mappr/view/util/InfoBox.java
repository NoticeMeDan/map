package com.noticemedan.mappr.view.util;

import javafx.scene.control.Alert;

public class InfoBox {
	private Alert alert = new Alert(Alert.AlertType.INFORMATION);

	public InfoBox(String text) {
		this.alert.setContentText(text);
		this.alert.setHeaderText(null);
		this.alert.setTitle("Info");
	}

	public void show() {
		this.alert.show();
	}
}
