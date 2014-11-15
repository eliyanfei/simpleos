package net.simpleframework.web.page.parser;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageUtils;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ParserUtils {

	static Element addScriptSRC(final PageParameter pageParameter, final Element element, String src) {
		if (!StringUtils.hasText(src)) {
			return null;
		}
		final Elements scripts;
		int size;
		final int p = src.indexOf("?");
		if (p > 0) { // 含有参数
			scripts = element.select("script[src=" + src + "]"); // 全部相等
			size = scripts.size();
		} else {
			scripts = element.select("script[src^=" + src + "]");
			size = scripts.size();
			if (size == 0) {
				src = PageUtils.addParameters(src, "v=" + PageUtils.pageContext.getVersion());
			}
		}
		if (size == 0) {
			return element.appendElement("script").attr("type", IPageConstants.SCRIPT_TYPE).attr("src", src);
		} else {
			return scripts.first();
		}
	}

	static Element addStylesheet(final PageParameter pageParameter, final Element element, String href) {
		if (!StringUtils.hasText(href)) {
			return null;
		}
		final Elements links;
		int size;
		final int p = href.indexOf("?");
		if (element.select("link[href*="+href.substring(href.lastIndexOf("/"), href.length())+"]").size() > 0) {
			return element;
		}
		if (p > 0) {
			links = element.select("link[href=" + href + "]");
			size = links.size();
		} else {
			links = element.select("link[href^=" + href + "]");
			size = links.size();
			if (size == 0) {
				href = PageUtils.addParameters(href, "v=" + PageUtils.pageContext.getVersion());
			}
		}
		if (size == 0) {
			return element.appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", href);
		} else {
			return links.first();
		}
	}

	static Element addScriptText(final Element element, final String js) {
		return addScriptText(element, js, true);
	}

	static Element addScriptText(final Element element, String js, final boolean compress) {
		js = StringUtils.blank(js);
		return element.appendElement("script").attr("type", IPageConstants.SCRIPT_TYPE)
				.appendChild(new DataNode(compress ? JavascriptUtils.jsCompress(js) : js.replaceAll("[\r\n\t]", ""), element.baseUri()));
	}
}
