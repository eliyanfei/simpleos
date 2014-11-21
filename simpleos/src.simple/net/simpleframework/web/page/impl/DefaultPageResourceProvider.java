package net.simpleframework.web.page.impl;

import java.util.Collection;

import javax.servlet.ServletContext;

import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.AbstractPageResourceProvider;
import net.simpleframework.web.page.IPageConstants;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultPageResourceProvider extends AbstractPageResourceProvider {

	public DefaultPageResourceProvider(final ServletContext servletContext) {
		super(servletContext);
	}

	private String resourceHomePath;

	@Override
	public String getResourceHomePath() {
		if (resourceHomePath == null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(PageUtils.getResourcePath());
			if (!"/".equals(sb.charAt(sb.length() - 1))) {
				sb.append("/");
			}
			sb.append(getName());
			resourceHomePath = sb.toString();
		}
		return resourceHomePath;
	}

	@Override
	public String getName() {
		return IPageConstants.DEFAULT_PAGE_RESOURCE_PROVIDER;
	}

	@Override
	public String[] getCssPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return new String[] { getCssSkin(requestResponse, "default.css") };
	}

	@Override
	public String[] getJavascriptPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		String[] jsArr = new String[] { PROTOTYPE_FILE, EFFECTS_FILE, "/js/core.js",
				"/js/simple_" + LocaleI18n.getLocale().toString() + ".js", "/js/simple.js" };
		if (HTTPUtils.isIE(requestResponse.request)) {
			jsArr = (String[]) LangUtils.add(jsArr, "/js/excanvas.js");
		}
		return jsArr;
	}

	@Override
	public String getInitJavascriptCode(final PageParameter pageParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("CONTEXT_PATH=\"").append(pageParameter.request.getContextPath()).append("\";");
		sb.append("HOME_PATH=CONTEXT_PATH + \"").append(getResourceHomePath()).append("\";");
		final String skin = pageParameter.pageDocument.getPageResourceProvider().getCurrentSkin(
				pageParameter);
		sb.append("SKIN=\"").append(skin).append("\";");
		sb.append("IS_EFFECTS=").append(PageUtils.pageConfig.isEffect(pageParameter.request))
				.append(";");
		return sb.toString();
	}
}
