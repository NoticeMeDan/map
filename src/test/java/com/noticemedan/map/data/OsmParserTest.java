package com.noticemedan.map.data;

import com.noticemedan.map.data.io.OsmMapData;
import com.noticemedan.map.data.osm.*;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class OsmParserTest {
	private String osmString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<osm>\n" +
			" <bounds minlat=\"55.6631000\" minlon=\"12.5730000\" maxlat=\"55.6804000\" maxlon=\"12.6031000\"/>\n" +
			" <node id=\"697547\" visible=\"true\" version=\"7\" changeset=\"3531468\" timestamp=\"2010-01-03T18:54:57Z\" user=\"Claus Hindsgaul\" uid=\"185440\" lat=\"55.6736517\" lon=\"12.5722541\">\n" +
			"  <tag k=\"highway\" v=\"traffic_signals\"/>\n" +
			" </node>\n" +
			" <way id=\"1813863\" visible=\"true\" version=\"6\" changeset=\"19598962\" timestamp=\"2013-12-23T12:11:58Z\" user=\"Hjart\" uid=\"207581\">\n" +
			"  <nd ref=\"697547\"/>\n" +
			"  <tag k=\"name\" v=\"Reykjaviksgade\"/>\n" +
			" </way>\n" +
			" <relation id=\"22836\" visible=\"true\" version=\"5\" changeset=\"28825121\" timestamp=\"2015-02-13T16:47:33Z\" user=\"Niels Elgaard Larsen\" uid=\"1288\">\n" +
			"  <member type=\"way\" ref=\"1813863\" role=\"outer\"/>\n" +
			"  <tag k=\"wikipedia\" v=\"da:Charlottenborg\"/>\n" +
			" </relation>" +
			"</osm>";

	private InputStream osmStream = new ByteArrayInputStream(osmString.getBytes(StandardCharsets.UTF_8));

	private Osm osmNode = new Osm(
			new Bounds(55.6631000, 12.5730000, 55.680400, 12.6031000),
			List.of(new Node(697547, 55.6736517, 12.5722541, List.of(new Tag("highway", "traffic_signals")))),
			List.of(new Way(1813863, List.of(new NodeProxy(697547)), List.of(new Tag("name", "Reykjaviksgade")))),
			Collections.emptyList(),
			List.of(new Relation(22836, List.of(new Member("way", 1813863, "outer")), List.of(new Tag("wikipedia", "da:Charlottenborg")))));

	@Test
	public void testParseOsmSync() {
		OsmMapData osmMapData = new OsmMapData();
		Osm testNode = osmMapData.deserialize(this.osmStream);
		assertNotNull(testNode);
		assertEquals(testNode, this.osmNode);
	}
}
