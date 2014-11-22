package net.simpleframework.web.page.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.core.id.ID;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IPageHandle;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageException;
import net.simpleframework.web.page.PageHtmlBuilder;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentBeanUtils;
import net.simpleos.SimpleosUtil;
import net.simpleos.backend.BackendUtils;
import net.simpleos.utils.StringsUtils;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleos.net
 */
public final class PageParser extends AbstractPageParser {
	private static PageHtmlBuilder htmlBuilder = PageUtils.pageContext.createPageHtmlBuilder();

	private Document htmlDocument;

	private Element headElement;

	public PageParser(final PageParameter pageParameter) {
		super(pageParameter);
	}

	public PageParser parser(final String responseString) {
		try {
			final PageParameter pageParameter = getPageParameter();
			ComponentBeanUtils.evalComponentBean(pageParameter);

			beforeCreate(pageParameter, responseString);

			final Collection<AbstractComponentBean> componentBeans = pageParameter.pageDocument.getComponentBeans(pageParameter);
			resourceBinding.doTag(pageParameter, headElement, componentBeans);

			normaliseNode(pageParameter, htmlDocument, componentBeans);

			htmlRender2Javascript.doTag(pageParameter, headElement, componentBeans);
			javascriptRender.doTag(pageParameter, headElement, componentBeans);

			pageLoaded.doTag(pageParameter, headElement, doPageHandle(pageParameter));
		} catch (final Exception e) {
			if (e instanceof ParserRuntimeException) {
				throw (ParserRuntimeException) e;
			} else {
				throw ParserRuntimeException.wrapException(e);
			}
		}
		return this;
	}

	private void beforeCreate(final PageParameter pageParameter, final String responseString) {
		if (pageParameter.isHttpRequest()) {
			htmlDocument = HTMLUtils.createHtmlDocument(responseString, true);
			headElement = htmlDocument.head();
			final Collection<String[]> coll = htmlBuilder.meta(pageParameter);
			if (coll != null) {
				if (SimpleosUtil.attrMap.get("site.site_keywords") != null)
					coll.add(new String[] { "name", "keywords", "content", SimpleosUtil.attrMap.get("site.site_keywords") });
				if (SimpleosUtil.attrMap.get("site.site_desc") != null)
					coll.add(new String[] { "name", "description", "content", SimpleosUtil.attrMap.get("site.site_desc") });
				for (final String[] attri : coll) {
					if (attri != null && attri.length > 1 && attri.length % 2 == 0) {
						final Element meta = headElement.prependElement("meta");
						for (int i = 0; i < attri.length; i += 2) {
							meta.attr(attri[i], attri[i + 1]);
						}
					}
				}
			}

			final PageDocument pageDocument = pageParameter.getPageDocument();
			final String title = pageDocument.getTitle(pageParameter);

			if (StringUtils.hasText(title)) {
				htmlDocument.title(title + "-" + StringsUtils.trimNull(SimpleosUtil.attrMap.get("site.site_name"), ""));
			} else {
				htmlDocument.title(StringsUtils.trimNull(SimpleosUtil.attrMap.get("site.site_name"), ""));
			}

			String favicon = pageDocument.getPageBean().getFavicon();
			if (!StringUtils.hasText(favicon)) {
				favicon = pageParameter.getContextPath() + "/simple/main/image/favicon.png?v=" + pageParameter.hashCode();
			}
			final Element link = htmlDocument.head().appendElement("link");
			link.attr("href", pageParameter.wrapContextPath(favicon));
			link.attr("rel", "SHORTCUT ICON");

			headElement.append(htmlBuilder.headStyle(pageParameter));
		} else {
			htmlDocument = HTMLUtils.createHtmlDocument(responseString, false);
			headElement = htmlDocument.select("head").first();
			if (headElement == null) {
				headElement = htmlDocument.createElement("head");
				htmlDocument.prependChild(headElement);
			}
			headElement.attr("move", "true");
		}
	}

	private static final String[] i18nAttributes = new String[] { "value", "title" };

	private void normaliseNode(final PageParameter pageParameter, final Element element, final Collection<AbstractComponentBean> componentBeans) {
		final float ieVersion = HTTPUtils.getIEVersion(pageParameter.request);
		for (final Node child : element.childNodes()) {
			if (child instanceof Element) {
				final String id = child.attr("id");
				if (StringUtils.hasText(id)) {
					htmlRender.doTag(pageParameter, htmlDocument.head(), (Element) child, componentBeans);
				}

				for (final String attribute : i18nAttributes) {
					final String value = child.attr(attribute);
					if (StringUtils.hasText(value)) {
						child.attr(attribute, LocaleI18n.replaceI18n(value));
					}
				}

				final String nodeName = child.nodeName();
				if ("a".equalsIgnoreCase(nodeName)) {
					child.attr("hidefocus", "hidefocus");
					final String href = child.attr("href");
					if (!StringUtils.hasText(href)) {
						if (ieVersion > 0f && ieVersion < 7.0f) {
							child.attr("href", "###");
						} else {
							child.attr("href", "javascript:void(0);");
						}
					} else {
						child.attr("href", pageParameter.wrapContextPath(href));
					}
				} else if ("form".equalsIgnoreCase(nodeName)) {
					if (!StringUtils.hasText(child.attr("action"))) {
						child.attr("action", "javascript:void(0);");
					}
				} else if ("img".equalsIgnoreCase(nodeName)) {
					final String src = child.attr("src");
					if (StringUtils.hasText(src)) {
						child.attr("src", pageParameter.wrapContextPath(src));
					}
				}
				normaliseNode(pageParameter, (Element) child, componentBeans);
			} else {
				if (child instanceof TextNode) {
					final String text = ((TextNode) child).getWholeText();
					if (StringUtils.hasText(text)) {
						((TextNode) child).text(LocaleI18n.replaceI18n(text));
					}
				} else if (child instanceof DataNode) {
					final String text = ((DataNode) child).getWholeData();
					if (StringUtils.hasText(text)) {
						child.attr("data", LocaleI18n.replaceI18n(text));
					}
				}
			}
		}
	}

	final class PageData {
		final Map<String, Object> dataBinding = new LinkedHashMap<String, Object>() {
			private static final long serialVersionUID = -5341646518048722337L;

			@Override
			public Object put(final String key, Object value) {
				if (value instanceof ID) {
					value = ((ID) value).getValue();
				}
				return super.put(key, value);
			}
		};

		List<String> visibleToggle = new ArrayList<String>();

		List<String> readonly = new ArrayList<String>();

		List<String> disabled = new ArrayList<String>();
	}

	private PageData doPageHandle(final PageParameter pageParameter) {
		final PageData pageData = new PageData();
		final IPageHandle pageHandle = pageParameter.getPageHandle();
		if (pageHandle != null) {
			final String handleMethod = pageParameter.getPageDocument().getPageBean().getHandleMethod();
			if (StringUtils.hasText(handleMethod)) {
				try {
					final Method methodObject = pageHandle.getClass().getMethod(handleMethod, PageParameter.class, Map.class, List.class, List.class,
							List.class);
					methodObject
							.invoke(pageHandle, pageParameter, pageData.dataBinding, pageData.visibleToggle, pageData.readonly, pageData.disabled);
				} catch (final Exception e) {
					throw PageException.wrapException(e);
				}
			} else {
				pageHandle.pageLoad(pageParameter, pageData.dataBinding, pageData.visibleToggle, pageData.readonly, pageData.disabled);
			}
		}
		return pageData;
	}

	public String toHtml(final PageParameter pageParameter) {
		if (htmlDocument == null) {
			return "";
		}
		String html = htmlDocument.html();
		if (pageParameter.isHttpRequest()) {
			boolean doctype = false;
			for (final Node child : htmlDocument.childNodes()) {
				if (child instanceof DocumentType) {
					doctype = true;
					break;
				}
			}
			if (!doctype) {
				html = htmlBuilder.doctype(getPageParameter()) + html;
			}
		}
		return html;
	}

	private static ResourceBinding resourceBinding = new ResourceBinding();
	private static HtmlRender htmlRender = new HtmlRender();
	private static JavascriptRender javascriptRender = new JavascriptRender();
	private static HtmlRender2Javascript htmlRender2Javascript = new HtmlRender2Javascript();
	private static PageLoaded pageLoaded = new PageLoaded();
}
