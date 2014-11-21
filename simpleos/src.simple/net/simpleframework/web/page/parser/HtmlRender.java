package net.simpleframework.web.page.parser;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.ComponentBeanUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHtmlRender;
import net.simpleframework.web.page.component.IComponentRender;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HtmlRender extends AbstractParser {
	void doTag(final PageParameter pageParameter, final Element htmlHead, final Element element,
			final Collection<AbstractComponentBean> componentBeans) {
		for (AbstractComponentBean componentBean : componentBeans) {
			componentBean = ComponentBeanUtils.getRunningComponentBean(pageParameter.getSession(),
					componentBean);
			if (!(componentBean instanceof AbstractContainerBean)) {
				continue;
			}

			final AbstractContainerBean containerBean = (AbstractContainerBean) componentBean;
			final ComponentParameter compParameter = ComponentParameter.get(pageParameter,
					componentBean);
			final String name = (String) compParameter.getBeanProperty("name");
			if (!StringUtils.hasText(name)) {
				continue;
			}

			final IComponentRender render = containerBean.getComponentRender();
			if (!(render instanceof IComponentHtmlRender)) {
				continue;
			}
			if (!((Boolean) compParameter.getBeanProperty("runImmediately"))) {
				continue;
			}

			final String tagId = element.attr("id");
			if (!StringUtils.hasText(tagId)) {
				continue;
			}
			if (!tagId.equals(compParameter.getBeanProperty("containerId"))) {
				continue;
			}

			doBeforeRender(compParameter);
			final String html = ((IComponentHtmlRender) render).getHtml(compParameter);
			if (!StringUtils.hasText(html)) {
				return;
			}

			final Document htmlDocument = HTMLUtils.createHtmlDocument(html, false);
			for (final Element moveHead : htmlDocument.select("head[move]")) {
				for (final Element link : moveHead.select("link[href], link[rel=stylesheet]")) {
					ParserUtils.addStylesheet(pageParameter, htmlHead, link.attr("href"));
					link.remove();
				}
				final StringBuilder jsCode = new StringBuilder();
				for (final Element script : moveHead.select("script")) {
					final String src = script.attr("src");
					if (StringUtils.hasText(src)) {
						ParserUtils.addScriptSRC(pageParameter, htmlHead, src);
					} else {
						jsCode.append(StringUtils.blank(script.data()));
					}
					script.remove();
				}
				if (jsCode.length() > 0) {
					ParserUtils.addScriptText(htmlHead, jsCode.toString());
				}
				if (moveHead.children().size() == 0) {
					moveHead.remove();
				}
			}
			element.empty();
			for (final Node child : new ArrayList<Node>(htmlDocument.childNodes())) {
				element.appendChild(child);
			}
		}
	}
}
