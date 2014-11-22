package net.simpleos.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public final class SaxUtils {
	public static final void parserXml(final InputStream inputStream, final DefaultHandler handle){
		try {
			parserXml(inputStream, handle, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @throws Exception
	 */
	public static final void parserXml1(final InputStream inputStream, final DefaultHandler handle) throws Exception{
		parserXml(inputStream, handle, true);
	}

	/**
	 */
	public static final void parserXml(final InputStream inputStream, final DefaultHandler handler, final boolean ignoreDomCheck)throws Exception {
		try {
			final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			if (ignoreDomCheck) {
				parserFactory.setValidating(false);
				parserFactory.setFeature("http://xml.org/sax/features/namespaces", false);
				parserFactory.setFeature("http://xml.org/sax/features/validation", false);
				parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
				parserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				parserFactory.setNamespaceAware(false);
			}
			final SAXParser saxParser = parserFactory.newSAXParser();
			saxParser.parse(inputStream, handler);
		} catch (final Exception e) {
			throw e;
			// do nothing
		} finally {
			if (null != inputStream)
				try {
					inputStream.close();
				} catch (final IOException e) {
				}
		}
	}
}
