package net.simpleframework.web.page.component.ui.pager;

import javax.servlet.ServletContext;

import net.simpleframework.web.page.component.AbstractComponentBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class GroupTablePagerRegistry extends TablePagerRegistry {

	public static final String groupTablePager = "groupTablePager";

	public GroupTablePagerRegistry(final ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public String getComponentName() {
		return groupTablePager;
	}

	@Override
	protected Class<? extends AbstractComponentBean> getBeanClass() {
		return GroupTablePagerBean.class;
	}
}