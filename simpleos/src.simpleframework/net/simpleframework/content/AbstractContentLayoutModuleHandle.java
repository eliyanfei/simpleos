package net.simpleframework.content;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
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
public abstract class AbstractContentLayoutModuleHandle extends AbstractPortalModuleHandle {

	public AbstractContentLayoutModuleHandle(final PageletBean pagelet) {
		super(pagelet);
	}

	@Override
	public OptionWindowUI getPageletOptionUI(final ComponentParameter compParameter) {
		return new OptionWindowUI(getPagelet()) {
			@Override
			public int getHeight() {
				return 400;
			}

			@Override
			public int getWidth() {
				return 360;
			}
		};
	}

	protected String doDateFormat(String forward, final String optionProperty) {
		final String dateFormat = getPagelet().getOptionProperty(optionProperty);
		if (StringUtils.hasText(dateFormat)) {
			forward = WebUtils.addParameters(forward, "dateFormat=" + dateFormat);
		}
		return forward;
	}

	protected String doDescLength(String forward, final String optionProperty) {
		final String[] vals = StringUtils.split(getPagelet().getOptionProperty(optionProperty), "*");
		if (vals != null && vals.length > 0) {
			forward = WebUtils.addParameters(forward, "descLength=" + ConvertUtils.toInt(vals[0], 0));
			if (vals.length > 1) {
				forward = WebUtils.addParameters(forward, "descNums=" + ConvertUtils.toInt(vals[1], 0));
			}
		}
		return forward;
	}
}
