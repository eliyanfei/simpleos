package net.itsite.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

/**
 * ʹ��SAX����XML�Ĺ�����
 * 
 * @author QianFei.Xu;E-Mail:qianfei.xu@rosense.cn
 * @time Jul 10, 2009 5:57:03 PM
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
	 * ���쳣��Ҫ�׳�ȥ����
	 * @throws Exception
	 */
	public static final void parserXml1(final InputStream inputStream, final DefaultHandler handle) throws Exception{
		parserXml(inputStream, handle, true);
	}

	/**
	 * ����XML,������ֻ��Ҫ���ľ������������
	 * 
	 * @param inputStream
	 *            ��Ҫ������Ŀ��������
	 * @param handler
	 *            ����ݽ��д����ʵ��
	 * @param ignoreDomCheck �����DOM�ж����DTD���﷨����
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
