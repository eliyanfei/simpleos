package net.simpleframework.web.page.component.ui.portal.module;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UrlModuleHandle extends AbstractPortalModuleHandle {

	public UrlModuleHandle(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_url_name=" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(getResourceHomePath() + "/jsp/module/url_option.jsp");
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		final String url = getPagelet().getOptionProperty("_url_name");
		if (StringUtils.hasText(url) && !url.equals(compParameter.request.getContextPath())) {
			return new UrlForward(url);
		} else {
			return null;
		}
	}

	@Override
	public OptionWindowUI getPageletOptionUI(final ComponentParameter compParameter) {
		return new OptionWindowUI(getPagelet()) {
			@Override
			public int getHeight() {
				return 120;
			}
		};
	}
}
