package com.noticemedan.mappr.view.util;

import io.vavr.control.Option;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePicker {
	private FileChooser picker = new FileChooser();

	public FilePicker(FileChooser.ExtensionFilter filter) {
		this.picker.setTitle("Vælg Fil");
		this.picker.getExtensionFilters().addAll(filter);
	}

	public Option<Path> getPath(Stage target) {
		Option<File> file = Option.of(this.picker.showOpenDialog(target));
		return file.map(x -> Paths.get(x.toURI()));
	}
}
