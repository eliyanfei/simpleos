package net.itsite.utils;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class MyEntityResolver implements EntityResolver {
	public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
		return new InputSource(new StringReader(""));
	}
}
