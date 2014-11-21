package net.simpleframework.web.page.component.ui.pager;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PagerPageLoad extends DefaultPageHandle {

	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("jobView".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = PagerUtils
					.getComponentParameter(pageParameter);
			if (nComponentParameter.componentBean != null) {
				return nComponentParameter.getBeanProperty("jobView");
			}
		} else if ("importPage".equals(beanProperty)) {
			return addImportPage(pageParameter, new String[] { TablePagerUtils.getHomePath()
					+ "/jsp/tablepager.xml" });
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}
}
