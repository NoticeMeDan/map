package com.noticemedan.map.data.io;

import com.noticemedan.map.model.Entities;
import com.noticemedan.map.model.OSMMaterialElement;
import com.noticemedan.map.model.osm.OSMNode;
import com.noticemedan.map.model.osm.OSMRelation;
import com.noticemedan.map.model.osm.OSMType;
import com.noticemedan.map.model.osm.OSMWay;
import com.noticemedan.map.model.utilities.LongToOSMNodeMap;
import com.noticemedan.map.model.utilities.OSMELementProperty;
import com.noticemedan.map.model.utilities.Rect;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class OSMReader {
	private List<OSMMaterialElement> osmMaterialElements = new LinkedList<>();

	private EnumMap<OSMType, List<OSMMaterialElement>> initializeMap() {
		EnumMap<OSMType, List<OSMMaterialElement>> map = new EnumMap<>(OSMType.class);
		for (OSMType type : OSMType.values()) {
			map.put(type, new ArrayList<>());
		}
		return map;
	}

	public List<OSMMaterialElement> getShapesFromFile(FileInputStream fileInputStream) {
		String filename = ".osm"; // TODO @Simon
		if (filename.endsWith(".osm")) {
			readFromOSM(new InputSource(fileInputStream));
		} else if (filename.endsWith(".zip")) {
			try {
				ZipInputStream zis = new ZipInputStream(fileInputStream);
				zis.getNextEntry();
				readFromOSM(new InputSource(zis));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (filename.endsWith(".bin")) {
			try {
				ObjectInputStream is = new ObjectInputStream(fileInputStream);
				osmMaterialElements = (List<OSMMaterialElement>) is.readObject();
				Entities.setMinLon((double) is.readObject());
				Entities.setMinLat((double) is.readObject());
				Entities.setMaxLon((double) is.readObject());
				Entities.setMaxLat((double) is.readObject());
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return osmMaterialElements;
	}

	public void readFromOSM(InputSource filename) {
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(new OSMHandler());
			xmlReader.parse(filename);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public void add(OSMType type, Shape shape) {
		OSMELementProperty osmeLementProperty = new OSMELementProperty();
		Rectangle2D shapeBounds = shape.getBounds2D();
		double x1 = shapeBounds.getX();
		double y1 = shapeBounds.getY();
		double xLength = shapeBounds.getWidth();
		double yLength = shapeBounds.getHeight();
		Rect rect = new Rect(x1, y1, (x1 + xLength), (y1 + yLength));
		OSMMaterialElement osmMaterialElement = new OSMMaterialElement();
		osmMaterialElement.setOsmType(type);
		osmMaterialElement.setBounds(rect);
		osmMaterialElement.setAvgPoint(rect.getAveragePoint());
		osmMaterialElement.setShape(shape);
		osmMaterialElement.setColor(osmeLementProperty.deriveColorFromType(type));
		osmMaterialElements.add(osmMaterialElement);
	}

	public class OSMHandler extends DefaultHandler {
		private EnumMap<OSMType, List<Shape>> shapes;
		LongToOSMNodeMap idToNode = new LongToOSMNodeMap(25);
		Map<Long, OSMWay> idToWay = new HashMap<>();
		HashMap<OSMNode, OSMWay> coastlines = new HashMap<>();
		OSMWay way;
		private double lonFactor;
		private OSMType type;
		private OSMRelation relation;

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
					idToNode.put(id, lonFactor * lon, -lat);
					break;
				case "way":
					way = new OSMWay();
					type = OSMType.UNKNOWN;
					idToWay.put(Long.parseLong(attributes.getValue("id")), way);
					break;
				case "relation":
					relation = new OSMRelation();
					type = OSMType.UNKNOWN;
					break;
				case "member":
					OSMWay w = idToWay.get(Long.parseLong(attributes.getValue("ref")));
					if (w != null) {
						relation.add(w);
					}
					break;
				case "tag":
					switch (attributes.getValue("k")) {
						case "highway":
							type = OSMType.ROAD;
							if (attributes.getValue("v").equals("primary")) {
								type = OSMType.HIGHWAY;
							}
							break;
						case "natural":
							if (attributes.getValue("v").equals("water")) {
								type = OSMType.WATER;
							}
							else if (attributes.getValue("v").equals("heath")) {
								type = OSMType.HEATH;
							}
							else if (attributes.getValue("v").equals("tree_row")) {
								type = OSMType.TREE_ROW;
							}
							else if (attributes.getValue("v").equals("grassland")) {
								type = OSMType.GRASSLAND;
							}
							else if (attributes.getValue("v").equals("coastline")) {
								type = OSMType.COASTLINE;
							}
							break;
						case "building":
							type = OSMType.BUILDING;
							break;
						default:
							break;
					}
					break;
				case "nd":
					way.add(idToNode.get(Long.parseLong(attributes.getValue("ref"))));
					break;
				default:
					break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			Path2D path = new Path2D.Double();
			OSMNode node;
			switch (qName) {
				case "way":
					if (type == OSMType.COASTLINE) {
						// stitch coastlines together
						// search for coastlines that can be merged with current way
						OSMWay before = coastlines.remove(way.from());
						OSMWay after = coastlines.remove(way.to());
						OSMWay merged = new OSMWay();

						// add these three paths together
						if (before != null) {
							merged.addAll(before.subList(0, before.size() - 1));
						}
						merged.addAll(way);
						if (after != null && after != before) {
							merged.addAll(after.subList(1, after.size()));
						}
						coastlines.put(merged.to(), merged);
						coastlines.put(merged.from(), merged);
					} else {
						node = way.get(0);
						path.moveTo(node.getLon(), node.getLat());
						for (int i = 1; i < way.size(); i++) {
							node = way.get(i);
							path.lineTo(node.getLon(), node.getLat());
						}
						add(type, path);
					}
					break;
				case "relation":
					for (OSMWay way : relation) {
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
					for (Map.Entry<OSMNode, OSMWay> coastline : coastlines.entrySet()) {
						OSMWay way = coastline.getValue();
						if (coastline.getKey() == way.from()) {
							path = new Path2D.Double();
							path.setWindingRule(Path2D.WIND_EVEN_ODD);
							node = way.get(0);
							path.moveTo(node.getLon(), node.getLat());
							for (int i = 1; i < way.size(); i++) {
								node = way.get(i);
								path.lineTo(node.getLon(), node.getLat());
							}
							add(OSMType.COASTLINE, path);
						}
					}
					break;
				default:
					break;
			}
		}
	}

}
