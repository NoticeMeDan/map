package com.noticemedan.mappr.dao;

import com.noticemedan.mappr.model.MapData;
import com.noticemedan.mappr.model.map.*;
import com.noticemedan.mappr.model.util.Coordinate;
import com.noticemedan.mappr.model.util.LongToOSMNodeMap;
import com.noticemedan.mappr.model.util.OsmElementProperty;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Vector;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipInputStream;

@NoArgsConstructor
@Slf4j
public class OsmDao implements DataReader<MapData> {
	private Vector<Element> elements = Vector.empty();
	private Vector<Element> coastlineElements = Vector.empty();
	private Vector<Address> addresses = Vector.empty();
	private Boundaries boundaries = new Boundaries();
	private String currentName;

	@Override
	public MapData read(Path input) throws IOException {
		String[] splitName = input.getFileName().toString().split(Pattern.quote("."));
		String type = splitName[splitName.length - 1];

		log.info("Begin reading from OSM");
		if (type.equals("osm")) {
			readFromOSM(new InputSource(Files.newBufferedReader(input)));
		} else if (type.equals("zip")) {
			try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(input))) {
				zip.getNextEntry();
				readFromOSM(new InputSource(zip));
			}
		}

		log.info("End reading from OSM");
		log.info("Elements: " + elements.length());
		log.info("Coastlines: " + coastlineElements.length());

		return MapData.builder()
				.boundaries(this.boundaries)
				.elements(this.elements.filter(x -> x.getType() != Type.UNKNOWN))
				.coastlineElements(this.coastlineElements)
				.addresses(this.addresses)
				.poi(List.empty())
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

	private void add(Type type, Path2D shape, int maxspeed) {
		Element osmElement = new Element();
		osmElement.setType(type);
		osmElement.setAvgPoint(new Coordinate(shape.getBounds2D().getCenterX(), shape.getBounds2D().getCenterY()));
		osmElement.setShape(shape);
		osmElement.setColor(OsmElementProperty.deriveColorFromType(type));
		osmElement.setMaxspeed(maxspeed);
		osmElement.setName(currentName);
		currentName = null;
		if (type.equals(Type.COASTLINE)) this.coastlineElements = coastlineElements.append(osmElement);
		else this.elements = elements.append(osmElement);
	}

	public class OsmHandler extends DefaultHandler {
		LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
		Map<Long, Vector<Node>> idToWay = HashMap.empty();
		Map<Node, Vector<Node>> coastlines = HashMap.empty();
		Address address = new Address();
		int path2DSize = 1;
		private Type type = Type.UNKNOWN;
		private long currentNodeID;
		private Vector<Node> osmWay;
		private Vector<Vector<Node>> osmRelation;
		private int maxspeed = 50; // default value
		final String regex = "\\d+";
		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		private long index;


		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			switch (qName) {
				case "bounds":
					double minLat = Double.parseDouble(attributes.getValue("minlat"));
					double minLon = Double.parseDouble(attributes.getValue("minlon"));
					double maxLat = Double.parseDouble(attributes.getValue("maxlat"));
					double maxLon = Double.parseDouble(attributes.getValue("maxlon"));
					boundaries.setMinLon(minLon);
					boundaries.setMaxLon(maxLon);
					boundaries.setMinLat(Coordinate.latToCanvasLat(minLat));
					boundaries.setMaxLat(Coordinate.latToCanvasLat(maxLat));
					break;
				case "node":
					double lon = Double.parseDouble(attributes.getValue("lon"));
					double lat = Double.parseDouble(attributes.getValue("lat"));
					long id = Long.parseLong(attributes.getValue("id"));
					currentNodeID = id;
					idToNode.put(id, lon, Coordinate.latToCanvasLat(lat));
					break;
				case "way":
					this.osmWay = Vector.empty();
					type = Type.UNKNOWN;
					this.index = Long.parseLong(attributes.getValue("id"));
					this.idToWay = idToWay.put(Long.parseLong(attributes.getValue("id")), osmWay);
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
						Node currentNode = idToNode.get(currentNodeID);
						type = Type.ADDRESS;
						address.setCoordinate(new Coordinate(currentNode.getLon(),currentNode.getLat()));
					}
					switch (keyValue) {
						case "maxspeed":
							final Matcher matcher = pattern.matcher(attributes.getValue("v"));
							while (matcher.find()) {
								this.maxspeed = Integer.parseInt(matcher.group(0));
							}
							break;
						case "highway":
							type = Type.UNCLASSIFIED;
							if (attributes.getValue("v").equals("motorway")) type = Type.MOTORWAY;
							if (attributes.getValue("v").equals("primary")) type = Type.PRIMARY;
							if (attributes.getValue("v").equals("trunk")) type = Type.TRUNK;
							if (attributes.getValue("v").equals("secondary")) type = Type.SECONDARY;
							if (attributes.getValue("v").equals("tertiary")) type = Type.TERTIARY;
							if (attributes.getValue("v").equals("footway")) type = Type.FOOTWAY;
							if (attributes.getValue("v").equals("footpath")) type = Type.FOOTPATH;
							if (attributes.getValue("v").equals("cycleway")) type = Type.CYCLEWAY;
							if (attributes.getValue("v").equals("track")) type = Type.TRACK;
							if (attributes.getValue("v").equals("service")) type = Type.SERVICE;
							if (attributes.getValue("v").equals("path")) type = Type.PATH;
							if (attributes.getValue("v").equals("road")) type = Type.ROAD;
							if (attributes.getValue("v").equals("residential")) type = Type.RESIDENTIAL;
							break;
						case "railway":
							if (attributes.getValue("v").equals("rail")) type = Type.RAIL;
							break;
						case "natural":
							if (attributes.getValue("v").equals("water")) type = Type.WATER;
							else if (attributes.getValue("v").equals("heath")) type = Type.HEATH;
							else if (attributes.getValue("v").equals("scrub")) type = Type.HEATH;
							else if (attributes.getValue("v").equals("wood")) type = Type.FOREST;
							else if (attributes.getValue("v").equals("tree_row")) type = Type.TREE_ROW;
							else if (attributes.getValue("v").equals("grassland")) type = Type.GRASSLAND;
							else if (attributes.getValue("v").equals("coastline")) type = Type.COASTLINE;
							break;
						case "leisure":
							if (attributes.getValue("v").equals("park")) type = Type.PARK;
							if (attributes.getValue("v").equals("common")) type = Type.PARK;
							break;
						case "amenity":
							if (attributes.getValue("v").equals("grave_yard")) type = Type.PARK;
							break;
						case "building":
							type = Type.BUILDING;
							break;
						case "landuse":
							if (attributes.getValue("v").equals("forest")) type = Type.FOREST;
							if (attributes.getValue("v").equals("village_green")) type = Type.GARDEN;
							if (attributes.getValue("v").equals("grass")) type = Type.GRASSLAND;
							break;
						case "aeroway":
							if (attributes.getValue("v").equals("aerodrome")) type = Type.AERODROME;
							if (attributes.getValue("v").equals("taxiway")) type = Type.TAXIWAY;
							if (attributes.getValue("v").equals("runway")) type = Type.RUNWAY;
							break;
						case "housenumber":
							address.setHouseNumber(attributes.getValue("v"));
							break;
						case "street":
							address.setStreet(attributes.getValue("v"));
							break;
						case "name":
							address.setName(attributes.getValue("v"));
							currentName = attributes.getValue("v");
							break;
						case "postcode":
							address.setPostcode(attributes.getValue("v"));
							break;
						case "city":
							address.setCity(attributes.getValue("v"));
							break;
						default:
							break;
					}
					break;
				case "nd":
					this.osmWay = osmWay.append(idToNode.get(Long.parseLong(attributes.getValue("ref"))));
					this.idToWay = idToWay.put(this.index,osmWay);
					this.path2DSize++;
					break;
				default:
					break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) {
			Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, this.path2DSize);
			this.path2DSize = 1;
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

						if(!this.osmWay.isEmpty()) {
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
						if(!merged.isEmpty()) {
							this.coastlines = coastlines.put(merged.get(merged.size() - 1), merged);
							this.coastlines = coastlines.put(merged.get(0), merged);
						}
					} else {
						node = this.osmWay.get(0);
						path.moveTo(node.getLon(), node.getLat());
						for (int i = 1; i < osmWay.size(); i++) {
							node = this.osmWay.get(i);
							path.lineTo(node.getLon(), node.getLat());
						}
						add(type, path, maxspeed);
					}
					break;
				case "relation":
					for (Vector<Node> way : osmRelation) {
						node = way.head();
						path.moveTo(node.getLon(), node.getLat());
						for(Node n : way) {
							if(n == way.head()) continue;
							node = n;
							path.lineTo(node.getLon(), node.getLat());
						}
					}
					add(type, path, maxspeed);
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
							add(Type.COASTLINE, path, 0);
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
