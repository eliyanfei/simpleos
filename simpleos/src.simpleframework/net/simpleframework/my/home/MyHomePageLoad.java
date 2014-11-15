package net.simpleframework.my.home;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.AbstractTitleAwarePageLoad;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyHomePageLoad extends AbstractTitleAwarePageLoad {
	@Override
	public Object getBeanProperty(final PageParameter pageParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			HomeTabsBean homeTabs = MyHomeUtils.getHomeTab(pageParameter
					.getRequestParameter(IHomeApplicationModule.MYTAB_ID));
			if (homeTabs == null) {
				homeTabs = MyHomeUtils.getFirstHomeTab(pageParameter);
			}
			final String title = wrapApplicationTitle(homeTabs.getTabText());
			if (title != null) {
				return title;
			}
		}
		return super.getBeanProperty(pageParameter, beanProperty);
	}

	public void tabEdit(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final HomeTabsBean homeTab = MyHomeUtils.getHomeTab(pageParameter
				.getRequestParameter("tab_id"));
		if (homeTab != null) {
			dataBinding.put("tab_name", homeTab.getTabText());
			dataBinding.put("tab_description", homeTab.getDescription());
			dataBinding.put("tab_id", homeTab.getId());
		}
	}
}
