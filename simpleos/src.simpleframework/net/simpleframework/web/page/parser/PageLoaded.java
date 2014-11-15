package net.simpleframework.web.page.parser;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageParameter;

import org.jsoup.nodes.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageLoaded {

	void doTag(final PageParameter pageParameter, final Element htmlHead,
			final PageParser.PageData pageData) {
		final Map<String, Object> json = new HashMap<String, Object>();
		if (pageData.dataBinding.size() > 0) {
			json.put("dataBinding", pageData.dataBinding);
		}
		if (pageData.visibleToggle.size() > 0) {
			json.put("visibleToggle", pageData.visibleToggle);
		}
		if (pageData.readonly.size() > 0) {
			json.put("readonly", pageData.readonly);
		}
		if (pageData.disabled.size() > 0) {
			json.put("disabled", pageData.disabled);
		}
		final StringBuilder sb = new StringBuilder();
		if (json.size() > 0) {
			sb.append("var pageData = ").append(JSONUtils.toJSON(json)).append(";");
		}
		final String jsLoadedCallback = pageParameter.pageDocument.getJsLoadedCallback(pageParameter);
		if (StringUtils.hasText(jsLoadedCallback)) {
			sb.append(jsLoadedCallback);
		} else if (json.size() > 0) {
			if (pageData != null) {
				if (pageData.dataBinding.size() > 0) {
					sb.append("$Actions.valueBinding(pageData.dataBinding);");
				}
				if (pageData.visibleToggle.size() > 0) {
					sb.append("pageData.visibleToggle.each(function(e) { $Actions.visibleToggle(e); });");
				}
				if (pageData.readonly.size() > 0) {
					sb.append("pageData.readonly.each(function(e) { $Actions.readonly(e); });");
				}
				if (pageData.disabled.size() > 0) {
					sb.append("pageData.disabled.each(function(e) { $Actions.disable(e); });");
				}
			}
		}

		if (sb.length() > 0) {
			ParserUtils.addScriptText(htmlHead, JavascriptUtils.wrapWhenReady(sb.toString()));
		}
	}
}
