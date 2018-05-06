package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.Entities;
import com.noticemedan.mappr.model.MapData;
import com.noticemedan.mappr.model.map.Element;
import com.noticemedan.mappr.model.map.Node;
import com.noticemedan.mappr.model.map.Type;
import com.noticemedan.mappr.model.util.LongToOSMNodeMap;
import com.noticemedan.mappr.model.util.OsmElementProperty;
import com.noticemedan.mappr.model.util.Rect;
import com.noticemedan.mappr.model.map.Address;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

@NoArgsConstructor
@Slf4j
public class OsmDao implements DataReader, Supplier<MapData> {
	private Vector<Vector<Element>> elements = Vector.empty();
	@Getter
	private Vector<Element> osmElements = Vector.empty();
	private Vector<Element> osmCoastlineElements = Vector.empty();
	@Getter
	private Vector<Address> addresses = Vector.empty();
	private Path input;
	private String filename;

	public OsmDao(Path input) {
		this.input = input;
	}

	@Override
	public MapData read(Path input) throws IOException {
		String[] splitName = input.getFileName().toString().split(Pattern.quote("."));
		String type = splitName[splitName.length - 1];
		log.info("Begin reading from OSM");

		// TODO: Fix me i suck (strategy pattern)
		if (type.equals("osm")) {
			readFromOSM(new InputSource(Files.newBufferedReader(input)));
		} else if (type.equals("zip")) {
			// Program will crash if zip exceeds 2^31 bytes (max array length)
			ZipInputStream zip = new ZipInputStream(Files.newInputStream(input));
			zip.getNextEntry();
			readFromOSM(new InputSource(zip));
		}

		log.info("End reading from OSM");
		log.info("Coastlines: " + osmCoastlineElements.length());

		return MapData.builder()
				.elements(this.osmElements)
				.coastlineElements(this.osmCoastlineElements)
				.addresses(this.addresses)
				.build();
	}

	private void readFromOSM(InputSource filename) {
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(new OsmHandler());
			xmlReader.parse(filename);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void add(Type type, Shape shape) {
		OsmElementProperty osmElementProperty = new OsmElementProperty();
		Rectangle2D shapeBounds = shape.getBounds2D();
		double x1 = shapeBounds.getX();
		double y1 = shapeBounds.getY();
		double xLength = shapeBounds.getWidth();
		double yLength = shapeBounds.getHeight();
		Rect rect = new Rect(x1, y1, (x1 + xLength), (y1 + yLength));
		Element element = new Element();
		element.setType(type);
		element.setBounds(rect);
		element.setAvgPoint(rect.getAveragePoint());
		element.setShape(shape);
		element.setColor(osmElementProperty.deriveColorFromType(type));
		if (type.equals(Type.COASTLINE)) {
			this.osmCoastlineElements = osmCoastlineElements.append(element);
		}
		else {
			this.osmElements = osmElements.append(element);
		}
	}

	@Override
	public MapData get() {
		MapData data = MapData.builder().build();
		try {
			data = this.read(this.input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public class OsmHandler extends DefaultHandler {
		LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
		Map<Long, Vector<Node>> idToWay = HashMap.empty();
		Map<Node, Vector<Node>> coastlines = HashMap.empty();
		Address address = new Address();
		private double lonFactor;
		private Type type;
		private long currentNodeID;

		private Vector<Node> osmWay;
		private Vector<Vector<Node>> osmRelation;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			switch (qName) {
				case "bounds":
					double minLat = Double.parseDouble(attributes.getValue("minlat"));
					double minLon = Double.parseDouble(attributes.getValue("minlon"));
					double maxLat = Double.parseDouble(attributes.getValue("maxlat"));
					double maxLon = Double.parseDouble(attributes.getValue("maxlon"));
					double avgLat = minLat + (maxLat - minLat) / 2;
					lonFactor = Math.cos(avgLat / 180 * Math.PI);
					Entities.setMinLon(minLon * lonFactor);
					Entities.setMaxLon(maxLon * lonFactor);
					Entities.setMaxLat(-maxLat);
					Entities.setMinLat(-minLat);
					break;
				case "node":
					double lon = Double.parseDouble(attributes.getValue("lon"));
					double lat = Double.parseDouble(attributes.getValue("lat"));
					long id = Long.parseLong(attributes.getValue("id"));
					type = Type.UNKNOWN;
					currentNodeID = id;
					idToNode.put(id, lonFactor * lon, -lat);
					break;
				case "way":
					this.osmWay = Vector.empty();
					type = Type.UNKNOWN;
					idToWay.put(Long.parseLong(attributes.getValue("id")), osmWay);
					break;
				case "relation":
					this.osmRelation = Vector.empty();
					type = Type.UNKNOWN;
					break;
				case "member":
					Vector<Node> way = idToWay
							.get(Long.parseLong(attributes.getValue("ref")))
							.getOrNull();

					if (way != null) {
						this.osmRelation = osmRelation.append(way);
					}
					break;
				case "tag":
					String keyValue = attributes.getValue("k");
					if (keyValue.contains("addr:")) {
						keyValue = keyValue.substring(5);
						type = Type.ADDRESS;
						Node currentNode = idToNode.get(currentNodeID);
						address.setLat(currentNode.getLat());
						address.setLon(currentNode.getLon());
					}

					switch (keyValue) {
						case "highway":
							type = Type.ROAD;
							if (attributes.getValue("v").equals("motorway")) type = Type.MOTORWAY;
							if (attributes.getValue("v").equals("primary")) type = Type.PRIMARY;
							if (attributes.getValue("v").equals("secondary")) type = Type.SECONDARY;
							if (attributes.getValue("v").equals("tertiary")) type = Type.TERTIARY;
							break;
						case "natural":
							if (attributes.getValue("v").equals("water")) type = Type.WATER;
							else if (attributes.getValue("v").equals("heath")) type = Type.HEATH;
							else if (attributes.getValue("v").equals("tree_row")) type = Type.TREE_ROW;
							else if (attributes.getValue("v").equals("grassland")) type = Type.GRASSLAND;
							else if (attributes.getValue("v").equals("grassland")) type = Type.FOREST;
							else if (attributes.getValue("v").equals("coastline")) type = Type.COASTLINE;
							break;
						case "leisure":
							if (attributes.getValue("v").equals("park")) type = Type.PARK;
							break;
						case "building":
							type = Type.BUILDING;
							break;
						case "landuse":
							if (attributes.getValue("v").equals("forest")) type = Type.FOREST;
							break;
						case "housenumber":
							address.setHouseNumber(attributes.getValue("v"));
							type = Type.ADDRESS;
							break;
						case "street":
							address.setStreet(attributes.getValue("v"));
							type = Type.ADDRESS;
							break;
						case "name":
							address.setName(attributes.getValue("v"));
							type = Type.ADDRESS;
							break;
						case "postcode":
							address.setPostcode(attributes.getValue("v"));
							type = Type.ADDRESS;
							break;
						case "city":
							address.setCity(attributes.getValue("v"));
							type = Type.ADDRESS;
							break;
						default:
							break;
					}
					break;
				case "nd":
					this.osmWay = osmWay.append(idToNode.get(Long.parseLong(attributes.getValue("ref"))));
					break;
				default:
					break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			Path2D path = new Path2D.Double();
			Node node;
			switch (qName) {
				case "way":
					if (type == Type.COASTLINE) {
						// stitch coastlines together
						// search for coastlines that can be merged with current osmWay
						Node from = null;
						Node to = null;
						Vector<Node> before = null;
						Vector<Node> after = null;
						Vector<Node> merged = Vector.empty();

						if(this.osmWay.size() > 0) {
							from = this.osmWay.get(0);
							to = this.osmWay.get(osmWay.size() - 1);
						}
						before = coastlines.get(from).getOrNull();
						this.coastlines = coastlines.remove(from);
						after = coastlines.get(to).getOrNull();
						this.coastlines = coastlines.remove(to);
						// add these three paths together
						if (before != null) {
							merged = merged.appendAll(before.subSequence(0, before.size() - 1));
						}
						merged = merged.appendAll(osmWay);
						if (after != null && after != before) {
							merged = merged.appendAll(after.subSequence(1, after.size()));
						}
						if(merged.size() > 0) {
							this.coastlines = coastlines.put(merged.get(merged.size() - 1), merged);
							this.coastlines = coastlines.put(merged.get(0), merged);
						}
					} else {
						if(this.osmWay.size() > 0) {
							node = this.osmWay.get(0);
							path.moveTo(node.getLon(), node.getLat());
							for (int i = 1; i < osmWay.size(); i++) {
								node = this.osmWay.get(i);
								path.lineTo(node.getLon(), node.getLat());
							}
							add(type, path);
						}
					}
					break;
				case "relation":
					for (Vector<Node> way : osmRelation) {
						node = way.get(0);
						path.moveTo(node.getLon(), node.getLat());
						for (int i = 1; i < way.size(); i++) {
							node = way.get(i);
							path.lineTo(node.getLon(), node.getLat());
						}
					}
					add(type, path);
					break;
				case "osm":
					// convert all coastlines found to paths
					for (Tuple2<Node, Vector<Node>> coastline : coastlines) {
						path = new Path2D.Double();
						path.setWindingRule(Path2D.WIND_EVEN_ODD);
						Vector<Node> way = coastline._2;
						Node key = coastline._1;

						if (key == way.get(way.size() - 1)) {
							node = way.get(0);
							path.moveTo(node.getLon(), node.getLat());
							for (int i = 1; i < way.size(); i++) {
								node = way.get(i);
								path.lineTo(node.getLon(), node.getLat());
							}
							add(Type.COASTLINE, path);
						}
					}
					break;

				case "node":
					if (type == Type.ADDRESS) {
						addresses = addresses.append(address);
						address = new Address();
					}
				default:
					break;
			}
		}
	}
}
