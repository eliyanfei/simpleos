package net.simpleframework.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IConstants {
	static final String NEWLINE = "\n";

	static final String RETURN = "\r";

	static final String RETURNNEWLINE = "\r\n";

	static final String TAB = "\t";

	/* null */

	final static String HTML_BLANK_STRING = "&nbsp;";

	final static byte[] NULL_BYTE_ARRAY = new byte[0];

	final static Enumeration<?> NULL_ENUMERATION = new Enumeration<Object>() {
		@Override
		public boolean hasMoreElements() {
			return false;
		}

		@Override
		public Object nextElement() {
			return null;
		}
	};

	final static Iterator<?> NULL_ITERATOR = new Iterator<Object>() {
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Object next() {
			return null;
		}

		@Override
		public void remove() {
		}
	};

	/* encoding */
	static final String UTF8 = "UTF-8";

	static final String ISO8859 = "ISO-8859-1";

	static final String GBK = "GBK";
}
