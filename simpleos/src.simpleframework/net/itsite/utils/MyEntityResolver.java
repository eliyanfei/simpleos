package net.itsite.utils;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MyEntityResolver implements EntityResolver {
	public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
		return new InputSource(new StringReader(""));
	}
}
