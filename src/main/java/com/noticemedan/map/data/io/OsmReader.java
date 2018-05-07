package com.noticemedan.map.data.io;

import com.noticemedan.map.model.Entities;
import com.noticemedan.map.model.OsmElement;
import com.noticemedan.map.model.osm.OsmNode;
import com.noticemedan.map.model.osm.OsmType;
import com.noticemedan.map.model.osm.Amenity;
import com.noticemedan.map.model.utilities.LongToOSMNodeMap;
import com.noticemedan.map.model.utilities.OsmElementProperty;
import com.noticemedan.map.model.utilities.Rect;
import com.noticemedan.map.view.Address;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.function.Supplier;
import java.util.zip.ZipInputStream;

@NoArgsConstructor
@Slf4j
public class OsmReader implements Supplier<Vector<Vector<OsmElement>>> {
	private Vector<Vector<OsmElement>> elements = Vector.empty();
	@Getter
	private Vector<OsmElement> osmElements = Vector.empty();
	private Vector<OsmElement> osmCoastlineElements = Vector.empty();
	@Getter
	private Vector<Address> addresses = Vector.empty();
	private InputStream inputStream;
	private String filename;

	public OsmReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Vector<Vector<OsmElement>> getShapesFromFile(InputStream inputStream, String filename) {
		this.filename = filename;
		if (filename.endsWith(".osm")) {
			log.info("Begin reading from OSM");
			readFromOSM(new InputSource(inputStream));
			log.info("End reading from OSM");
		} else if (filename.endsWith(".zip")) {
			try {
				ZipInputStream zis = new ZipInputStream(inputStream);
				zis.getNextEntry();
				readFromOSM(new InputSource(zis));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (filename.endsWith(".bin")) {
			try {
				ObjectInputStream is = new ObjectInputStream(inputStream);
				osmElements = (Vector<OsmElement>) is.readObject();
				osmCoastlineElements = (Vector<OsmElement>) is.readObject();
				Entities.setMinLon((double) is.readObject());
				Entities.setMinLat((double) is.readObject());
				Entities.setMaxLon((double) is.readObject());
				Entities.setMaxLat((double) is.readObject());
			} catch (IOException | ClassNotFoundException e) {
				log.error(e.getStackTrace().toString());
			}
		}

		this.elements = elements.append(osmElements);
		this.elements = elements.append(osmCoastlineElements);
		return elements;
	}

	public void readFromOSM(InputSource filename) {
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(new OsmHandler());
			xmlReader.parse(filename);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void add(OsmType type, Amenity amenity, Shape shape) {
		OsmElementProperty osmElementProperty = new OsmElementProperty();
		Rectangle2D shapeBounds = shape.getBounds2D();
		double x1 = shapeBounds.getX();
		double y1 = shapeBounds.getY();
		double xLength = shapeBounds.getWidth();
		double yLength = shapeBounds.getHeight();
		Rect rect = new Rect(x1, y1, (x1 + xLength), (y1 + yLength));
		OsmElement osmElement = new OsmElement();
		osmElement.setOsmType(type);
		osmElement.setAmenity(amenity);
		osmElement.setBounds(rect);
		osmElement.setAvgPoint(rect.getAveragePoint());
		osmElement.setShape(shape);
		osmElement.setColor(osmElementProperty.deriveColorFromType(type));
		if (type.equals(OsmType.COASTLINE))
			this.osmCoastlineElements = osmCoastlineElements.append(osmElement);
		else
			this.osmElements = osmElements.append(osmElement);
	}

	@Override
	public Vector<Vector<OsmElement>> get() {
		return this.getShapesFromFile(this.inputStream, this.filename);
	}

	public class OsmHandler extends DefaultHandler {
		LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
		Map<Long, Vector<OsmNode>> idToWay = HashMap.empty();
		Map<OsmNode, Vector<OsmNode>> coastlines = HashMap.empty();
		Address address = new Address();
		int path2DSize = 1;
		private double lonFactor;
		private OsmType type;
		private Amenity amenity;
		private long currentNodeID;

		private Vector<OsmNode> osmWay;
		private Vector<Vector<OsmNode>> osmRelation;

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
					currentNodeID = id;
					idToNode.put(id, lonFactor * lon, -lat);
					break;
				case "way":
					this.osmWay = Vector.empty();
					type = OsmType.UNKNOWN;
					idToWay.put(Long.parseLong(attributes.getValue("id")), osmWay);
					break;
				case "relation":
					this.osmRelation = Vector.empty();
					type = OsmType.UNKNOWN;
					break;
				case "member":
					Vector<OsmNode> way = idToWay
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
						type = OsmType.ADDRESS;
						OsmNode currentNode = idToNode.get(currentNodeID);
						address.setLat(currentNode.getLat());
						address.setLon(currentNode.getLon());
					}

					switch (keyValue) {
						case "highway":
							type = OsmType.ROAD;
							if (attributes.getValue("v").equals("motorway")) type = OsmType.MOTORWAY;
							if (attributes.getValue("v").equals("primary")) type = OsmType.PRIMARY;
							if (attributes.getValue("v").equals("secondary")) type = OsmType.SECONDARY;
							if (attributes.getValue("v").equals("tertiary")) type = OsmType.TERTIARY;
							break;
						case "natural":
							if (attributes.getValue("v").equals("water")) type = OsmType.WATER;
							else if (attributes.getValue("v").equals("heath")) type = OsmType.HEATH;
							else if (attributes.getValue("v").equals("tree_row")) type = OsmType.TREE_ROW;
							else if (attributes.getValue("v").equals("grassland")) type = OsmType.GRASSLAND;
							else if (attributes.getValue("v").equals("grassland")) type = OsmType.FOREST;
							else if (attributes.getValue("v").equals("coastline")) type = OsmType.COASTLINE;
							break;
						case "leisure":
							if (attributes.getValue("v").equals("park")) type = OsmType.PARK;
							break;
						case "amenity":
							if(attributes.getValue("v").equals("cafe")) amenity = Amenity.CAFE;
							else if(attributes.getValue("v").equals("bank")) amenity = Amenity.BANK;
							else if(attributes.getValue("v").equals("bar")) amenity = Amenity.BAR;
							else if(attributes.getValue("v").equals("bus_station")) amenity = Amenity.BUS_STATION;
							else if(attributes.getValue("v").equals("college")) amenity = Amenity.COLLEGE;
							else if(attributes.getValue("v").equals("fast_food")) amenity = Amenity.FAST_FOOD;
							else if(attributes.getValue("v").equals("fuel")) amenity = Amenity.FUEL;
							else if(attributes.getValue("v").equals("hospital")) amenity = Amenity.HOSPITAL;
							else if(attributes.getValue("v").equals("kindergarten")) amenity = Amenity.KINDERGARTEN;
							else if(attributes.getValue("v").equals("library")) amenity = Amenity.LIBRARY;
							else if(attributes.getValue("v").equals("marketplace")) amenity = Amenity.MARKETPLACE;
							else if(attributes.getValue("v").equals("parking_entrance")) amenity = Amenity.PARKING_ENTRANCE;
							else if(attributes.getValue("v").equals("police")) amenity = Amenity.POLICE;
							else if(attributes.getValue("v").equals("pub")) amenity = Amenity.PUB;
							else if(attributes.getValue("v").equals("school")) amenity = Amenity.SCHOOL;
							else if(attributes.getValue("v").equals("toilets")) amenity = Amenity.TOILETS;
							else if(attributes.getValue("v").equals("university")) amenity = Amenity.UNIVERSITY;
							else if(attributes.getValue("v").equals("ferry_terminal")) amenity = Amenity.FERRY_TERMINAL;
							else if(attributes.getValue("v").equals("restaurant")) amenity = Amenity.RESTAURANT;
							else if(attributes.getValue("v").equals("ice_cream")) amenity = Amenity.ICE_CREAM;
							else amenity = null;
							break;
						case "building":
							type = OsmType.BUILDING;
							break;
						case "landuse":
							if (attributes.getValue("v").equals("forest")) type = OsmType.FOREST;
							break;
						case "housenumber":
							address.setHouseNumber(attributes.getValue("v"));
							break;
						case "street":
							address.setStreet(attributes.getValue("v"));
							break;
						case "name":
							address.setName(attributes.getValue("v"));
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
					this.path2DSize++;
					break;
				default:
					break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			Path2D path = new Path2D.Double(Path2D.WIND_EVEN_ODD, this.path2DSize);
			this.path2DSize = 1;
			OsmNode node;
			switch (qName) {
				case "way":
					if (type == OsmType.COASTLINE) {
						// stitch coastlines together
						// search for coastlines that can be merged with current osmWay
						OsmNode from = null;
						OsmNode to = null;
						Vector<OsmNode> before = null;
						Vector<OsmNode> after = null;
						Vector<OsmNode> merged = Vector.empty();

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
						if(!this.osmWay.isEmpty()) {
							node = this.osmWay.get(0);
							path.moveTo(node.getLon(), node.getLat());
							for (int i = 1; i < osmWay.size(); i++) {
								node = this.osmWay.get(i);
								path.lineTo(node.getLon(), node.getLat());
							}
							add(type, amenity, path);
						}
					}
					break;
				case "relation":
					for (Vector<OsmNode> way : osmRelation) {
						node = way.get(0);
						path.moveTo(node.getLon(), node.getLat());
						for (int i = 1; i < way.size(); i++) {
							node = way.get(i);
							path.lineTo(node.getLon(), node.getLat());
						}
					}
					add(type, null, path);
					break;
				case "osm":
					// convert all coastlines found to paths
					for (Tuple2<OsmNode, Vector<OsmNode>> coastline : coastlines) {
						path = new Path2D.Double();
						path.setWindingRule(Path2D.WIND_EVEN_ODD);
						Vector<OsmNode> way = coastline._2;
						OsmNode key = coastline._1;

						if (key == way.get(way.size() - 1)) {
							node = way.get(0);
							path.moveTo(node.getLon(), node.getLat());
							for (int i = 1; i < way.size(); i++) {
								node = way.get(i);
								path.lineTo(node.getLon(), node.getLat());
							}
							add(OsmType.COASTLINE, null, path);
						}
					}
					break;

				case "node":
					if (type == OsmType.ADDRESS) {
						addresses = addresses.append(address);
					}
				default:
					break;
			}
		}
	}
}
