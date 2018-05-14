package com.noticemedan.mappr.model.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Objects;

/**
 * With courtesy of StackOverflow:
 * https://stackoverflow.com/questions/15713119/java-nio-file-path-for-a-classpath-resource
 *
 * Fixes issue with getting a Path to a Jar resource.
 * Apparently Oracle are sitting on their hands all day.
 * https://bugs.java.com/bugdatabase/view_bug.do?bug_id=7181278
 */
public class JarHelper {
	public static Path resourceToPath(URL resource) throws IOException, URISyntaxException {

		Objects.requireNonNull(resource, "Resource URL cannot be null");
		URI uri = resource.toURI();

		String scheme = uri.getScheme();
		if (scheme.equals("file")) {
			return Paths.get(uri);
		}

		if (!scheme.equals("jar")) {
			throw new IllegalArgumentException("Cannot convert to Path: " + uri);
		}

		String s = uri.toString();
		int separator = s.indexOf("!/");
		String entryName = s.substring(separator + 2);
		URI fileURI = URI.create(s.substring(0, separator));

		FileSystem fs = FileSystems.newFileSystem(fileURI,
				Collections.<String, Object>emptyMap());
		return fs.getPath(entryName);
	}
}
