package net.simpleframework.organization.component.userpager;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

public class UserPagerPageLoad extends DefaultPageHandle {
	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("importCSS".equals(beanProperty)) {
			return new String[] { UserPagerUtils.cssUserutils(pageParameter) };
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

}
