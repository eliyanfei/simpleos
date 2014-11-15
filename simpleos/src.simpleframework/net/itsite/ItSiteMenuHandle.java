package net.itsite;

import java.util.Collection;

import net.itsite.permission.PlatformUtis;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.AbstractMenuHandle;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

public class ItSiteMenuHandle extends AbstractMenuHandle {

	@Override
	public Collection<MenuItem> getMenuItems(ComponentParameter compParameter, MenuItem menuItem) {
		/*if (menuItem != null) {
			return null;
		}
		final MenuBean menuBean = (MenuBean) compParameter.componentBean;
		final ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>(menuBean.getMenuItems());
		if (!OrgUtils.isMember(IJob.sj_manager, compParameter.getSession())) {
			menuItems.remove(menuItems.size() - 1);
		}
		return menuItems;*/
		if (menuItem != null) {
			return null;
		}
		IAccount account = AccountSession.getLogin(compParameter.getSession());
		return PlatformUtis.getPaltformMenusByUser(account == null ? null : account.user(), false);
	}

	@Override
	public void handleCreated(PageRequestResponse requestResponse, AbstractComponentBean componentBean) {
		super.handleCreated(requestResponse, componentBean);
	}

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}
}
