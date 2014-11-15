package net.simpleframework.desktop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.prj.manager.menu.PrjMenuBean;
import net.prj.manager.menu.PrjMenuUtils;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.StringUtils;

public class DesktopMgr {
	private static DesktopMgr desktopMgr = new DesktopMgr();
	private Map<String, DesktopBean> desktopMap = new LinkedHashMap<String, DesktopBean>();
	public Map<String, List<DesktopBean>> desktopDefaultMap = new HashMap<String, List<DesktopBean>>();
	public List<DesktopBean> toolBarList = new ArrayList<DesktopMgr.DesktopBean>();

	public static class DesktopBean {
		public String icon;
		public String url;
		public String uiName;
		public String text;
		public String name;
		public String param;
		public String action;
		public int idx;
		public boolean toolbar;
	}

	public Map<String, DesktopBean> getDesktopMap() {
		return desktopMap;
	}

	public Map<String, DesktopBean> getPermissionDesktopMap(HttpServletRequest request) {
		Map<String, DesktopBean> map = (Map<String, DesktopBean>) request.getSession().getAttribute("menu");
		try {
			if (map == null) {
				map = new LinkedHashMap<String, DesktopBean>();
				request.getSession().setAttribute("menu", map);
			} else {
				if (map.size() > 0)
					return map;
			}

			IAccount account = AccountSession.getLogin(request.getSession());
			if (account == null) {
				return map;
			}

			Map<String, DesktopBean> desktopMap = DesktopMgr.getDesktopMgr().getDesktopMap();
			for (String name : desktopMap.keySet()) {
				DesktopBean desktopBean = desktopMap.get(name);
				if (desktopBean != null)
					map.put(desktopBean.name, desktopBean);
			}
		} finally {
		}
		return map;
	}

	public DesktopBean getDesktopBean(final String name) {
		return this.desktopMap.get(name);
	}

	private DesktopMgr() {
	}

	private void buildMenu(Object parentId) {
		final IQueryEntitySet<PrjMenuBean> qs = PrjMenuUtils.appModule.queryBean("mark=0 and parentid=" + parentId + " order by oorder",
				PrjMenuBean.class);
		PrjMenuBean menu = null;
		List<DesktopBean> list = null;
		while ((menu = qs.next()) != null) {
			DesktopBean desktopBean = new DesktopBean();
			final StringBuffer buf = new StringBuffer();
			desktopBean.toolbar = false;
			desktopBean.text = menu.getText();
			desktopBean.name = menu.getName();
			desktopBean.param = "";
			desktopBean.uiName = menu.getName();
			desktopBean.icon = "/frame/template/t100/themes/app/config.png";
			desktopBean.url = menu.getUrl();
			desktopBean.idx = 0;
			buf.append("$Actions['").append(desktopBean.name + "Win").append("']('win=true&");
			if (desktopBean.toolbar) {
				toolBarList.add(desktopBean);
			}
			if (desktopBean.param != null) {
				buf.append(desktopBean.param);
			}
			buf.append("');");
			if (StringUtils.hasText(desktopBean.text)) {
				buf.append(desktopBean.text);
			}
			desktopBean.action = buf.toString();

			desktopMap.put(desktopBean.name, desktopBean);
			list = desktopDefaultMap.get(desktopBean.uiName);
			if (list == null) {
				list = new ArrayList<DesktopMgr.DesktopBean>();
				desktopDefaultMap.put(desktopBean.uiName, list);
			}
			list.add(desktopBean);
			buildMenu(menu.getId());
		}
	}

	public void init() {
		try {
			buildMenu(0);
		} catch (Exception e) {
		}
	}

	public static DesktopMgr getDesktopMgr() {
		return desktopMgr;
	}

}
