package com.noticemedan.mappr.model;

import com.noticemedan.mappr.dao.OsmDao;
import com.noticemedan.mappr.model.map.Address;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.service.ForestService;
import com.noticemedan.mappr.model.service.TextSearchService;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class DomainFacade {
	// Domain data
	public MapData mapData; // TODO Do something better than public

	// Services
	private TextSearchService<Address> addressSearch;
	private ForestService forestService;

	public DomainFacade() {
		try {
			Path path = Paths.get(DomainFacade.class.getResource("/fyn.osm.zip").toURI());
			this.initialize(path);
		} catch (Exception e) {
			log.error("An error occured", e);
		}
	}

	private void initialize(Path path) {
		try {
			this.mapData = new OsmDao().read(path); // Switch to MapData
		} catch (IOException e) {
			log.error("An error occured", e);
		}
		this.forestService = new ForestService(
				this.mapData.getElements().toJavaList(),
				this.mapData.getCoastlineElements().toJavaList());
		this.addressSearch = new TextSearchService<>(HashMap.ofEntries(
				this.mapData.getAddresses().map(x -> Tuple.of(x.toFullAddress(), x))));
	}

	// Viewport Data
	public List<Element> getCoastLines() { return this.forestService.getCoastlines(); }
	public List<Element> doRangeSearch(Rect area) { return this.forestService.rangeSearch(area); }
	public List<Element> doRangeSearch(Rect area, double zoom) { return this.forestService.rangeSearch(area, zoom); }

	// Address Search
	public io.vavr.collection.List<String> doAddressSearch(String search) {
		return this.addressSearch.search(search)
				.map(Tuple2::_1);
	}
}
