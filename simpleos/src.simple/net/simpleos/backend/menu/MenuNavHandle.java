package net.simpleos.backend.menu;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.AbstractMenuHandle;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 菜单的组成方式
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-2下午02:38:26
 */
public class MenuNavHandle extends AbstractMenuHandle {

	@Override
	public Collection<MenuItem> getMenuItems(ComponentParameter compParameter, MenuItem menuItem) {
		if (menuItem != null) {
			return null;
		}
		final Collection<MenuItem> menuList = new ArrayList<MenuItem>();
		final MenuBean menuNBean = (MenuBean) compParameter.componentBean;
		buildMenu(menuList, menuNBean, 0);
		return menuList;
	}

	private void buildMenu(final Collection<MenuItem> menuList, final MenuBean menuBean, Object parentId) {
		final IQueryEntitySet<MenuNavBean> qs = MenuNavUtils.appModule.queryBean("mark=0 and parentid=" + parentId + " order by oorder",
				MenuNavBean.class);
		MenuNavBean menu = null;
		while ((menu = qs.next()) != null) {
			MenuItem item = new MenuItem(menuBean);
			item.setTitle(menu.getText());
			if (menu.getUrl() != null) {
				final StringBuffer url = new StringBuffer();
				if (!menu.getUrl().startsWith("/") && !menu.getUrl().startsWith("http")) {
					url.append("/");
				}
				if (!menu.isBuildIn()) {
					if (!menu.getUrl().startsWith("http")) {
						url.append("/c");
					}
				}
				url.append(menu.getUrl());
				item.setUrl(url.toString());
			}
			menuList.add(item);
			buildMenu(item.getChildren(), menuBean, menu.getId());
		}
	}
}
