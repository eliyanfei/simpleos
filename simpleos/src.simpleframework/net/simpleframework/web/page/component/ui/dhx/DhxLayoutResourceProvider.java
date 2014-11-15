package net.simpleframework.web.page.component.ui.dhx;

import java.util.Collection;

import net.simpleframework.util.LangUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentResourceProvider;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 *@Description 
 *@Date 2012-11-28
 *@author lxy
 */
public class DhxLayoutResourceProvider extends AbstractComponentResourceProvider {

	public DhxLayoutResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String[] getJavascriptPath(final PageRequestResponse requestResponse, final Collection<AbstractComponentBean> componentBeans) {
		String[] JAVASCRIPT_PATH = new String[] { "/js/dhtmlxcommon.js", "/js/dhtmlxcontainer.js", "/js/dhtmlxlayout.js" };
		for (AbstractComponentBean compBean : componentBeans) {
			if (compBean instanceof DhxLayoutBean) {
				String pattern = ((DhxLayoutBean) compBean).getPattern().toString().substring(1);
				if (DhxLayoutUtils.additionalPattern.indexOf(pattern) > -1) {
					JAVASCRIPT_PATH = (String[]) LangUtils.add(JAVASCRIPT_PATH, "/patterns/dhtmlxlayout_pattern" + pattern.toLowerCase() + ".js");
				}
			}
		}

		return JAVASCRIPT_PATH;
	}

	@Override
	public String[] getCssPath(PageRequestResponse requestResponse, Collection<AbstractComponentBean> componentBeans) {
		String[] CSS_PATH = new String[] { "/css/dhtmlxlayout.css" };
		for (AbstractComponentBean compBean : componentBeans) {
			if (compBean instanceof DhxLayoutBean) {
				String skin = ((DhxLayoutBean) compBean).getSkin().toString();
				CSS_PATH = (String[]) LangUtils.add(CSS_PATH, "/skins/dhtmlxlayout_dhx_" + skin + ".css");
			}
		}
		return CSS_PATH;
	}

}