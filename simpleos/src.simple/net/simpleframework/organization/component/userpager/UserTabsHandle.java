package net.simpleframework.organization.component.userpager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tabs.AbstractTabsHandle;
import net.simpleframework.web.page.component.ui.tabs.TabItem;
import net.simpleframework.web.page.component.ui.tabs.TabsBean;
import net.simpleos.SimpleosUtil;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserTabsHandle extends AbstractTabsHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("activeIndex".equals(beanProperty)) {
			return ConvertUtils.toInt(compParameter.getRequestParameter("activeIndex"), 0);
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Collection<TabItem> getTabItems(ComponentParameter compParameter) {
		final TabsBean tabsBean = (TabsBean) compParameter.componentBean;
		final List<TabItem> colls = new ArrayList<TabItem>(tabsBean.getTabItems());
		if (!colls.isEmpty() && !SimpleosUtil.isManage(compParameter, SimpleosUtil.applicationModule)) {
//			colls.remove(0);
		}
		return colls;
	}
}
