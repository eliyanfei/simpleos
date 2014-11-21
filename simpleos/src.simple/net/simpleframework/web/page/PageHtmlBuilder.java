package net.simpleframework.web.page;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.util.HTTPUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageHtmlBuilder extends ALoggerAware {

	public final static String HTML5_DOC_TYPE = "<!DOCTYPE HTML>";

	public final static String HTML401_DOC_TYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";

	public final static String XHTML10_DOC_TYPE = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";

	public String doctype(final PageParameter pageParameter) {
		return HTML5_DOC_TYPE;
	}

	public String headStyle(final PageParameter pageParameter) {
		final StringBuilder style = new StringBuilder();
		style.append("<style type=\"text/css\">body * {font-family:");
		if (gtWindowXP(pageParameter.request)) {
			style.append("'微软雅黑','Microsoft YaHei','宋体'");
		}
		style.append("Verdana,");
		style.append("Tahoma, Arial, Sans-Serif;}</style>");
		return style.toString();
	}

	public Collection<String[]> meta(final PageParameter pageParameter) {
		final ArrayList<String[]> al = new ArrayList<String[]>();
		al.add(new String[] { "http-equiv", "Content-Type", "content", "text/html; charset=" + PageUtils.pageConfig.getCharset() });
		if (HTTPUtils.isIE(pageParameter.request)) {
			// EmulateIE7
			al.add(new String[] { "http-equiv", "X-UA-Compatible", "content", "IE=10" });
		}
		return al;
	}

	protected boolean gtWindowXP(final HttpServletRequest request) {
		Boolean gt = (Boolean) request.getSession().getAttribute("__gtWindowXP");
		if (gt != null) {
			return gt.booleanValue();
		}
		try {
			final String userAgent = HTTPUtils.getUserAgent(request);
			int p = userAgent.indexOf("Windows NT");
			if (p > 0) {
				p += 11;
				final StringBuilder sb = new StringBuilder();
				while (true) {
					final char c = userAgent.charAt(p++);
					if (Character.isDigit(c) || c == '.') {
						sb.append(c);
					} else {
						break;
					}
				}
				request.getSession().setAttribute("__gtWindowXP", (gt = Float.parseFloat(sb.toString()) >= 6.0f));
				return gt;
			}
		} catch (final Exception e) {
		}
		return false;
	}
}
