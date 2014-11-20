package net.itsite;

import java.util.Map;

import net.itsite.permission.PermissionBean;
import net.itsite.user.CounterBean;
import net.itsite.user.SchoolCatalog;
import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.my.home.DefaultHomeApplicationModule;
import net.simpleframework.my.home.HomeTabsBean;
import net.simpleframework.web.page.PageRequestResponse;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:58:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class ItSiteHomeApplicationModule extends DefaultHomeApplicationModule {

	@Override
	public String getTabUrl(final PageRequestResponse requestResponse, final HomeTabsBean homeTab) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/home/").append(homeTab.getId()).append(".html");
		return sb.toString();
	}

	@Override
	public void init(IInitializer initializer) {
		super.init(initializer);
		ItSiteUtil.applicationModule = this;
	}

	@Override
	protected void putTables(Map<Class<?>, Table> tables) {
		super.putTables(tables);
		tables.put(SchoolCatalog.class, new Table("simple_school"));
		tables.put(CounterBean.class, new Table("it_counter"));
		tables.put(PermissionBean.class, new Table("it_platform_permission"));
	}
}
