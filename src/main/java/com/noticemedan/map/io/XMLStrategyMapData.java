package com.noticemedan.map.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.noticemedan.map.osm.Osm;
import lombok.Cleanup;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;


public class XMLStrategyMapData implements MapDataIOReader {
	@Override
	public Osm deserialize(String path) throws XMLStreamException, IOException {
		File file = new File(path);
		XmlMapper xmlMapper = new XmlMapper();
		String xml = inputStreamToString(new FileInputStream(file));
		Osm value = xmlMapper.readValue(xml, Osm.class);
		return value;
	}

	public static String inputStreamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}
}
