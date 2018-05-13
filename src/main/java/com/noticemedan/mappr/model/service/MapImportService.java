package com.noticemedan.mappr.model.service;

import com.noticemedan.mappr.dao.MapDao;
import com.noticemedan.mappr.dao.OsmDao;
import com.noticemedan.mappr.model.MapData;
import javafx.concurrent.Task;
import lombok.AllArgsConstructor;

import java.nio.file.Path;

@AllArgsConstructor
public class MapImportService extends Task<MapData> {
	Path from;
	Path to;
	OsmDao osmDao;
	MapDao mapDao;

	@Override
	protected MapData call() throws Exception {
		MapData temp = osmDao.read(from);
		return mapDao.write(to, temp);
	}
}
