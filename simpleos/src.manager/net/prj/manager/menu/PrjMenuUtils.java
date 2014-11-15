package net.prj.manager.menu;

import net.a.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.IQueryEntitySet;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class PrjMenuUtils {
	public static IPrjMenuAppModule appModule = null;

	public static void loadMenu() {
		buildMenu(0);
	}

	private static void buildMenu(Object parentId) {
		final IQueryEntitySet<PrjMenuBean> qs = PrjMenuUtils.appModule.queryBean("mark=0 and parentid=" + parentId + " order by oorder",
				PrjMenuBean.class);
		PrjMenuBean menu = null;
		while ((menu = qs.next()) != null) {
			if (StringsUtils.isNotBlank1(menu.getUrl()))
				ItSiteUtil.menuMap.put(menu.getUrl().substring(0, menu.getUrl().lastIndexOf(".")), menu.getText());
			buildMenu(menu.getId());
		}
	}

}
