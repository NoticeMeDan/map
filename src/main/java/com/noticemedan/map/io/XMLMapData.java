package com.noticemedan.map.io;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.noticemedan.map.osm.Osm;
import lombok.Cleanup;

import javax.xml.stream.XMLStreamException;
import java.io.*;


public class XMLMapData implements MapDataIOReader {
	private static String inputStreamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		@Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(is));

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	@Override
	public Osm deserialize(File file) throws XMLStreamException, IOException {
		JacksonXmlModule module = new JacksonXmlModule();
		module.setDefaultUseWrapper(false);
		XmlMapper xmlMapper = new XmlMapper(module);
		String xml = inputStreamToString(new FileInputStream(file));
		Osm value = xmlMapper.readValue(xml, Osm.class);
		return value;
	}
}
