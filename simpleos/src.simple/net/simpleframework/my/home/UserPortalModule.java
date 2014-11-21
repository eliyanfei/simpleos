package net.simpleframework.my.home;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.PageletBean;
import net.simpleframework.web.page.component.ui.portal.module.AbstractPortalModuleHandle;
import net.simpleframework.web.page.component.ui.portal.module.OptionWindowUI;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserPortalModule extends AbstractPortalModuleHandle {
	public UserPortalModule(final PageletBean pagelet) {
		super(pagelet);
	}

	private static String[] defaultOptions = new String[] { "_user_photo_dimension=128*128" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter compParameter) {
		return new UrlForward(MyHomeUtils.deployPath + "jsp/user_layout_option.jsp");
	}

	@Override
	public IForward getPageletContent(final ComponentParameter compParameter) {
		String forward = MyHomeUtils.deployPath + "jsp/user_layout.jsp";
		final String dimension = getPagelet().getOptionProperty("_user_photo_dimension");
		if (StringUtils.hasText(dimension)) {
			final String[] dimensionArr = StringUtils.split(dimension, "*");
			if (dimensionArr.length > 0) {
				forward = WebUtils.addParameters(forward, "width=" + dimensionArr[0]);
			}
			if (dimensionArr.length > 1) {
				forward = WebUtils.addParameters(forward, "height=" + dimensionArr[1]);
			}
		}
		return new UrlForward(forward);
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
