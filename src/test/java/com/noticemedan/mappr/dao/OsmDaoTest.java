package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.Rect;
import io.vavr.collection.Vector;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OsmDaoTest {
	private OsmDao osmDao = new OsmDao();
	private String osmString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<map>\n" +
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
			"</map>";

	private InputStream osmStream = new ByteArrayInputStream(osmString.getBytes(StandardCharsets.UTF_8));

//	@Test
//	public void testGetShapesFromFile() {
//		this.osmDao.read(osmStream);
//		Vector<Element> osmElements = this.osmDao.getOsmElements();
//		assertEquals(osmElements.length(), 2);
//
//		Element element = osmElements.get(0);
//		assertEquals(element.getAvgPoint(), new Coordinate(7.089912651806818, -55.6736517));
//		assertEquals(element.getBounds(), new Rect(7.089912651806818, -55.6736517, 7.089912651806818, -55.6736517));
//		assertEquals(element.getType(), Type.UNKNOWN);
//		assertTrue(element.isDepthEven());
//	}
}
