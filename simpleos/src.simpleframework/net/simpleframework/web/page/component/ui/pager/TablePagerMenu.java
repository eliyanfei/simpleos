package net.simpleframework.web.page.component.ui.pager;

import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.AbstractMenuHandle;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.pager.db.IDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerMenu extends AbstractMenuHandle {

	@Override
	public Collection<MenuItem> getMenuItems(final ComponentParameter compParameter,
			final MenuItem menuItem) {
		if (menuItem != null) {
			return null;
		}
		final ComponentParameter nComponentParameter = PagerUtils
				.getComponentParameter(compParameter);
		return ((IDbTablePagerHandle) nComponentParameter.getComponentHandle()).getContextMenu(
				nComponentParameter, (MenuBean) compParameter.componentBean);
	}
}
