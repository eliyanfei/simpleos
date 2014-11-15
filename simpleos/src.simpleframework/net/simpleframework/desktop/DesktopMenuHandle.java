package net.simpleframework.desktop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.desktop.DesktopMgr.DesktopBean;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.AbstractMenuHandle;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 
 * @author yanfei.li
 */
public class DesktopMenuHandle extends AbstractMenuHandle {

	@Override
	public Collection<MenuItem> getMenuItems(ComponentParameter compParameter, MenuItem menuItem) {
		if (menuItem != null) {
			return null;
		}
		final MenuBean menuBean = (MenuBean) compParameter.componentBean;
		Map<String, DesktopBean> desktopMap = DesktopMgr.getDesktopMgr().getPermissionDesktopMap(compParameter.request);
		Collection<MenuItem> items = new ArrayList<MenuItem>();
		for (DesktopBean bean : desktopMap.values()) {
			MenuItem subitem = new MenuItem(menuBean);
			subitem.setTitle(bean.text);
			subitem.setJsSelectCallback("$('now_ui_name').value='" + bean.name + "';$desktop.hideFaskBar('" + bean.name + "__');" + bean.action);
			String iconName = bean.icon.substring(bean.icon.lastIndexOf("/") + 1, bean.icon.length());
			subitem.setIcon(bean.icon.replaceAll(iconName, "16" + iconName));
			items.add(subitem);
		}
		return items;
	}

	public String jsCode(ComponentParameter compParameter) {
		Collection<MenuItem> menus = this.getMenuItems(compParameter, null);
		StringBuilder jsCode = new StringBuilder("var menus = [");
		int i = 0;
		for (MenuItem item : menus) {
			if (i > 0)
				jsCode.append(",");
			jsCode.append("{");
			resverJsCode(item, jsCode);
			jsCode.append("}");
			i++;
		}
		jsCode.append("];");
		return jsCode.toString();
	}

	public void resverJsCode(MenuItem item, StringBuilder jsCode) {
		jsCode.append("'text':'").append(item.getTitle()).append("'");
		if (StringUtils.hasText(item.getIcon()))
			jsCode.append(",'icon':'").append(item.getIcon()).append("'");
		if (StringUtils.hasText(item.getJsSelectCallback())) {
			jsCode.append(",'click':function(){").append(item.getJsSelectCallback()).append("}");
		}
		if (item.getChildren().size() > 0) {
			jsCode.append(",'clildren':[");
			int i = 0;
			for (MenuItem item1 : item.getChildren()) {
				if (i > 0)
					jsCode.append(",");
				jsCode.append("{");
				resverJsCode(item1, jsCode);
				jsCode.append("}");
				i++;
			}
			jsCode.append("]");
		}
	}
}
