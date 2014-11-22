package net.simpleos.backend.menu;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleos.SimpleosUtil;
import net.simpleos.utils.StringsUtils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午10:09:54 
 *
 */
public class MenuNavUtils {
	public static IMenuNavAppModule appModule = null;

	public static void loadMenu() {
		buildMenu(0);
	}

	private static void buildMenu(Object parentId) {
		final IQueryEntitySet<MenuNavBean> qs = MenuNavUtils.appModule.queryBean("mark=0 and parentid=" + parentId + " order by oorder",
				MenuNavBean.class);
		MenuNavBean menu = null;
		while ((menu = qs.next()) != null) {
			if (StringsUtils.isNotBlank1(menu.getUrl()))
				SimpleosUtil.menuMap.put(menu.getUrl().substring(0, menu.getUrl().lastIndexOf(".")), menu.getText());
			buildMenu(menu.getId());
		}
	}

}
