package net.simpleframework.util;

import java.util.Collection;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class StringUtils {
	private static final String defaultDelimiter = ";";

	public static String[] split(final String str, final String delimiters) {
		return org.apache.commons.lang3.StringUtils.split(str, delimiters);
	}

	public static String[] split(final String str) {
		return split(str, defaultDelimiter);
	}

	public static String join(final Collection<?> c, final String delim) {
		return org.apache.commons.lang3.StringUtils.join(c.iterator(), delim);
	}

	public static String join(final Collection<?> c) {
		return join(c, defaultDelimiter);
	}

	public static String join(final Object[] arr, final String delim) {
		return org.apache.commons.lang3.StringUtils.join(arr, delim);
	}

	public static String join(final Object[] arr) {
		return join(arr, defaultDelimiter);
	}

	public static String replace(final String inString, final String oldPattern,
			final String newPattern) {
		return org.apache.commons.lang3.StringUtils.replace(inString, oldPattern, newPattern);
	}

	public static boolean hasText(final String string) {
		return org.apache.commons.lang3.StringUtils.isNotBlank(string);
	}

	public static boolean hasTextObject(final Object object) {
		if (object == null) {
			return false;
		}
		if (object instanceof String) {
			return hasText((String) object);
		}
		return true;
	}

	public static String text(final String... strings) {
		if (strings != null) {
			for (final String string : strings) {
				if (hasText(string)) {
					return string;
				}
			}
		}
		return "";
	}

	public static String blank(final Object object) {
		return object == null ? "" : ConvertUtils.toString(object);
	}

	public static String encodeHex(final byte[] b) {
		return new String(Hex.encodeHex(b));
	}

	public static byte[] decodeHex(final String s) {
		try {
			return Hex.decodeHex(s.toCharArray());
		} catch (final DecoderException e) {
			return IConstants.NULL_BYTE_ARRAY;
		}
	}

	public static String decodeHexString(final String s) {
		return new String(decodeHex(s));
	}

	public static String hash(final Object object) {
		if (object == null) {
			return null;
		}
		final int hash = object.hashCode();
		return hash > 0 ? String.valueOf(hash) : "0" + Math.abs(hash);
	}

	public static String getFilename(final String path) {
		return FilenameUtils.getName(path);
	}

	public static String getFilenameExtension(final String path) {
		return FilenameUtils.getExtension(path);
	}

	public static String stripFilenameExtension(final String path) {
		if (path == null) {
			return null;
		}
		final int sepIndex = path.lastIndexOf(".");
		return (sepIndex != -1 ? path.substring(0, sepIndex) : path);
	}

	public static String substring(final String str, final int length) {
		return substring(str, length, false);
	}

	public static String substring(String str, final int length, final boolean dot) {
		str = blank(str).trim();
		if (length >= str.length()) {
			return str;
		} else {
			str = str.substring(0, length);
			if (dot) {
				str += "...";
			}
			return str;
		}
	}

	public static String trimOneLine(String str) {
		for (final String c : new String[] { IConstants.NEWLINE, IConstants.RETURN, IConstants.TAB }) {
			str = replace(str, c, "");
		}
		return str.trim();
	}
}
