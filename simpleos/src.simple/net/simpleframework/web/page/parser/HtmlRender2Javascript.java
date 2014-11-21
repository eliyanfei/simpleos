package net.simpleframework.web.page.parser;

import java.util.Collection;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentBeanUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHtmlRender;
import net.simpleframework.web.page.component.IComponentRender;

import org.jsoup.nodes.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HtmlRender2Javascript extends AbstractParser {

	void doTag(final PageParameter pageParameter, final Element htmlHead,
			final Collection<AbstractComponentBean> componentBeans) {
		for (AbstractComponentBean componentBean : componentBeans) {
			componentBean = ComponentBeanUtils.getRunningComponentBean(pageParameter.getSession(),
					componentBean);
			final ComponentParameter compParameter = ComponentParameter.get(pageParameter,
					componentBean);
			final String name = (String) compParameter.getBeanProperty("name");
			if (!StringUtils.hasText(name)) {
				continue;
			}

			final IComponentRender render = componentBean.getComponentRender();
			if (!(render instanceof IComponentHtmlRender)) {
				continue;
			}

			final String js = ((IComponentHtmlRender) render).getHtmlJavascriptCode(compParameter);
			if (StringUtils.hasText(js)) {
				// 立即执行，此处不用wrapWhenReady
				// 该js要优先于HttpClient产生的jscode
				ParserUtils.addScriptText(htmlHead, JavascriptUtils.wrapFunction(js));
			}
		}
	}
}
