package net.simpleframework.content;

import net.simpleframework.content.component.topicpager.ITopicPagerHandle;
import net.simpleframework.content.component.topicpager.TopicBean;
import net.simpleframework.core.AAttributeAware;
import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.SmileyUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ContentUtils {
	static final String ELLIPSIS = "<span style=\"color: black;\">&hellip;</span>";

	public static String KEY_CONTENT_DOCUMENT = "jsoup_content_document";

	public static IGetAccountAware getAccountAware() {
		return MySpaceUtils.getAccountAware();
	}

	public static Document getContentDocument(final IContentBeanAware bean) {
		final AAttributeAware attri = (AAttributeAware) bean;
		if (attri == null) {
			return HTMLUtils.createHtmlDocument("", true);
		}
		Document doc = (Document) attri.getAttribute(KEY_CONTENT_DOCUMENT);
		if (doc == null) {
			attri.setAttribute(KEY_CONTENT_DOCUMENT, doc = HTMLUtils.createHtmlDocument(bean.getContent(), false));
		}
		return doc;
	}

	public static String getShortContent(final IContentBeanAware bean, final int length, final boolean newLine) {
		if (bean instanceof IDescriptionBeanAware) {
			String desc = ((IDescriptionBeanAware) bean).getDescription();
			if (StringUtils.hasText(desc)) {
				desc = StringUtils.substring(desc, length, true);
				if (newLine) {
					desc = HTMLUtils.convertHtmlLines(desc);
				}
				return desc;
			}
		}
		return HTMLUtils.truncateHtml(getContentDocument(bean), length, newLine, true, true);
	}

	public static String getContentImage(final ComponentParameter compParameter, final Object bean) {
		return getContentImage(compParameter, bean, 96, 96);
	}

	public static String getContentImage(final ComponentParameter compParameter, final Object bean, final int width, final int height) {
		final IContentPagerHandle pHandle = (IContentPagerHandle) compParameter.getComponentHandle();
		IContentBeanAware contentAware = null;
		if (bean instanceof IContentBeanAware) {
			contentAware = (IContentBeanAware) bean;
		} else if (bean instanceof TopicBean) {
			contentAware = ((ITopicPagerHandle) pHandle).getPostsText(compParameter, bean);
		}
		if (contentAware == null) {
			return null;
		}
		final Document doc = getContentDocument(contentAware);
		final Element img = doc.select("img").first();
		if (img == null) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("<img class=\"photo_icon\" style=\"width: " + width + "px; height: " + height + "px;\" src=\"");
		sb.append(pHandle.getFileCache(compParameter).getImagePath(compParameter, img.attr("src"), width, height));
		sb.append("\" />");
		return sb.toString();
	}

	public static String doTextContent(final String content) {
		return doTextContent(content, true);
	}

	public static String doTextContent(final String content, final boolean smiley) {
		String c = HTMLUtils.htmlEscape(StringUtils.blank(content));
		if (smiley)
			c = SmileyUtils.replaceSmiley(c);
		c = HTMLUtils.convertHtmlLines(c);
		return "<p>" + c + "</p>";
	}
}
