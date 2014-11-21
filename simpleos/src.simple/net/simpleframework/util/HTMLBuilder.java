package net.simpleframework.util;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */

public abstract class HTMLBuilder {
	public static String SEP = "<span style=\"margin: 0px 4px;\">|</span>";

	public static String NAV = "<span style=\"margin: 0px 2px;\">&raquo;</span>";

	public static String div(final String text, final String[] attris) {
		return tag("DIV", text, attris);
	}

	public static String div(final String[] attris) {
		return div("", attris);
	}

	public static String inputHidden(final String name, final Object value) {
		return input("hidden", name, value);
	}

	public static String input(final String type, final String name, final Object value) {
		return tag("INPUT", (String) null, new String[] { "type=" + type, "id=" + name,
				"name=" + name, "value=" + value });
	}

	public static String a(final String text, final String[] attris) {
		return tag("A", text, attris);
	}

	public static String tag(final String tagName, final String[] attris) {
		return tag(tagName, (String) null, attris);
	}

	public static String tag(final String tagName, final String text, final String[] attris) {
		if (!StringUtils.hasText(tagName)) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<").append(tagName);
		if (attris != null) {
			for (final String value : attris) {
				final int i = value.indexOf("=");
				if (i > 0) {
					sb.append(" ").append(value.substring(0, i)).append("=\"");
					sb.append(HTMLUtils.htmlEscape(value.substring(i + 1)));
					sb.append("\"");
				}
			}
		}
		if (text != null) {
			sb.append(">");
			sb.append(text).append("</").append(tagName).append(">");
		} else {
			sb.append("/>");
		}
		return sb.toString();
	}
}
