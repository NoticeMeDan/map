package com.noticemedan.mappr.model;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.service.Forest;
import com.noticemedan.mappr.model.service.TextSearch;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DomainFacade {
	// Domain data
	public MapData mapData; // Do something better than making this public

	// Services
	private TextSearch addressSearch;
	private Forest forest; // Come up with a better name

	public DomainFacade() {
		this.mapData = new MapData(); // Switch to TestMapData
		this.forest = new Forest(
				this.mapData.getOsmElements().toJavaList(),
				this.mapData.getOsmCoastlineElements().toJavaList());
		this.addressSearch = new TextSearch<>(HashMap.ofEntries(
				this.mapData.getAddresses().map(x -> Tuple.of(x.toFullAddress(), x))));
	}

	// Forest
	public List<Element> getCoastLines() { return this.forest.getCoastlines(); }
	public List<Element> doRangeSearch(Rect area) { return this.forest.rangeSearch(area); }
	public List<Element> doRangeSearch(Rect area, double zoom) { return this.forest.rangeSearch(area, zoom); }
}
