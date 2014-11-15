//$Id: UUIDHexGenerator.java 8049 2005-08-30 23:28:50Z turin42 $
package net.itsite.utils;

import java.io.Serializable;
import java.util.Properties;

/**
 * <b>uuid</b><br>
 * <br>
 * A <tt>UUIDGenerator</tt> that returns a string of length 32, This string will consist of only hex digits. Optionally, the string may be
 * generated with separators between each component of the UUID.
 * 
 * Mapping parameters supported: separator.
 * 
 * @author Gavin King
 */

public class UUIDHexGenerator extends AbstractUUIDGenerator {
	public static final UUIDHexGenerator DEFAULT = new UUIDHexGenerator();
	private String sep = "";

	protected String format(final int intval) {
		final String formatted = Integer.toHexString(intval);
		final StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	protected String format(final short shortval) {
		final String formatted = Integer.toHexString(shortval);
		final StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	public Serializable generate(final Object obj) {
		return new StringBuffer(36).append(format(getIP())).append(sep).append(format(getJVM())).append(sep).append(format(getHiTime())).append(sep)
				.append(format(getLoTime())).append(sep).append(format(getCount())).toString();
	}

	public void configure(final Properties params) {
		sep = params.getProperty("separator", "");
	}

	public static final String generator() {
		return String.valueOf(UUIDHexGenerator.DEFAULT.generate(null));
	}

	public static final String generator(final Object obj) {
		return String.valueOf(UUIDHexGenerator.DEFAULT.generate(obj));
	}

	public static void main0(final String[] args) {
		System.out.println(UUIDHexGenerator.generator());
		System.out.println(UUIDHexGenerator.generator());
	}
}
