package net.simpleframework.desktop;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.a.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.desktop.DesktopMgr.DesktopBean;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;

/**
 */
public class DesktopUIUtils {
	public static DesktopUIApplicationModule application;

	private static Map<String, String> tabList = new LinkedHashMap<String, String>();
	static {
		tabList.put("ui-tab1", "桌面1");
		tabList.put("ui-tab2", "桌面2");
		tabList.put("ui-tab3", "桌面3");
		tabList.put("ui-tab4", "所有");
	}

	public static String buildTabs(PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		for (int i = 1; i < 5; i++) {
			buf.append("<a href=\"#\" class=\"ui-navbar " + (i == 1 ? "currTab" : "") + "\" title=\"桌面" + i + "\" nextId=\"ui-tab" + (i + 1)
					+ "\" preId=\"ui-tab" + (i - 1) + "\" id=\"ui-tab" + i + "\" appId=\"\">" + i + "</a>");
		}
		return buf.toString();
	}

	public static String getAllTheme(final PageRequestResponse requestResponse) {
		final IUser user = ItSiteUtil.getLoginUser(requestResponse);
		String path = requestResponse.request.getContextPath() + requestResponse.request.getRealPath("/frame/template/t100/themes/theme");
		final StringBuffer buf = new StringBuffer();
		buf.append("var skins =  new Array(\"10gaotie5.jpg\"");
		new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()) {
					buf.append(",\"").append(pathname.getName()).append("\"");
				}
				return false;
			}
		});
		new File(path + "/" + user.getId()).listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()) {
					buf.append(",\"").append(user.getId()).append("/").append(pathname.getName()).append("\"");
				}
				return false;
			}
		});
		buf.append(");");
		return buf.toString();
	}

	public static String buildTheme(final PageRequestResponse requestResponse, final boolean custom) {
		IAccount account = AccountSession.getLogin(requestResponse.getSession());
		String path = requestResponse.request.getRealPath("/frame/template/t100/themes/theme") + "/" + (custom ? account.getId() : "");
		final String p = requestResponse.request.getContextPath() + "/frame/template/t100/themes/theme/" + (custom ? (account.getId() + "/") : "");
		final StringBuffer buf = new StringBuffer();
		new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.isFile()) {
					buf.append("<div style='position: relative;' onclick=\"document.body.style.backgroundImage ='url(" + p + pathname.getName()
							+ ")'; $desktop.setCookies('desktop_theme', '" + p + pathname.getName() + "');\">");
					buf.append("<img src='" + p + pathname.getName() + "' width='120px' height='120px'>");
					if (custom)
						buf.append("<span class='desktopTheme' onclick=\"$Actions['delDesktopTheme']('fileName=" + pathname.getName()
								+ "');\"></span>");
					buf.append("</div>");
				}
				return false;
			}
		});
		return buf.toString();
	}

	public static Map<String, String> listUIName(PageRequestResponse requestResponse) {
		final Map<String, String> map = new HashMap<String, String>();
		ITableEntityManager tMgr = DesktopUIUtils.application.getDataObjectManager();
		IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account != null) {
			IQueryEntitySet<DesktopUIBean> qs = tMgr.query(new ExpressionValue("userId=? ", new Object[] { account.getId() }), DesktopUIBean.class);
			DesktopUIBean uiBean = null;
			while ((uiBean = qs.next()) != null) {
				map.put(uiBean.getUiName(), tabList.get(uiBean.getTabId()));
			}
		}
		return map;
	}

	public static String buildDesktop(PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		ITableEntityManager tMgr = DesktopUIUtils.application.getDataObjectManager();
		IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account == null) {
			return "";
		}
		String oorder = WebUtils.getCookie(requestResponse.request, "oorder");
		if (StringsUtils.isNotBlank1(oorder)) {
			oorder = "," + oorder;
		} else {
			oorder = "";
		}
		IQueryEntitySet<DesktopUIBean> qs = tMgr.query(new ExpressionValue("userId=? order by tabId " + oorder, new Object[] { account.getId() }),
				DesktopUIBean.class);
		DesktopUIBean uiBean = null;
		Map<String, List<DesktopUIBean>> map = new HashMap<String, List<DesktopUIBean>>();
		while ((uiBean = qs.next()) != null) {
			List<DesktopUIBean> list = map.get(uiBean.getTabId());
			if (list == null) {
				list = new ArrayList<DesktopUIBean>();
				map.put(uiBean.getTabId(), list);
			}
			list.add(uiBean);
		}
		Map<String, DesktopBean> sessionMenuMap = DesktopMgr.getDesktopMgr().getPermissionDesktopMap(requestResponse.request);

		for (String tab : tabList.keySet()) {
			if ("ui-tab4".equals(tab)) {
				continue;
			}
			buf.append("<div id='" + tab + "__' class=\"deskIcon ui-droppable\" style=\"margin-left: 85px;width:300px;\">");
			List<DesktopBean> list1 = DesktopMgr.getDesktopMgr().desktopDefaultMap.get(tab);
			if (list1 != null)
				for (DesktopBean bean : list1) {
					buf.append("<div class=\"desktop_icon ui-draggable ui-droppable titem\" title=\"" + bean.text
							+ "\" style=\"margin: 0px; left:20px;top:40px;position: absolute;\"");
					buf.append(" onclick=\"$('now_ui_name').value='" + bean.uiName + "';" + bean.action + "\">");
					buf.append("<span class=\"icon\"><img id='ui_" + bean.uiName + "' src=\"" + bean.icon + "\"> </span>");
					buf.append("<div class=\"textdesc\">" + (StringUtils.substring(bean.text, 5)) + "<s></s></div>");
					buf.append("</div>");
				}
			List<DesktopUIBean> list = map.get(tab);
			if (list != null)
				for (DesktopUIBean bean : list) {
					DesktopBean desktopBean = DesktopMgr.getDesktopMgr().getDesktopBean(bean.getUiName());
					if (desktopBean != null) {
						if (sessionMenuMap.containsKey(desktopBean.name)) {
							buf.append("<div class=\"desktop_icon ui-draggable ui-droppable titem\" title=\"" + desktopBean.text + "\" rowid=\""
									+ bean.getUiName() + "\" style=\"margin: 0px;left:20px;top:40px;position: absolute;\"");
							buf.append(" onclick=\"$('now_ui_name').value='" + bean.getUiName() + "';$desktop.hideFaskBar('" + desktopBean.name
									+ "__');" + desktopBean.action + "\">");
							buf.append("<span class=\"icon\"><img id='ui_" + bean.getUiName() + "' src=\"" + desktopBean.icon + "\"> </span>");
							buf.append("<div class=\"textdesc\">" + (StringUtils.substring(desktopBean.text, 5)) + "<s></s></div>");
							buf.append("</div>");
						}

					}
				}
			buf.append("<div class=\"desktop_icon add_icon\" style=\"margin: 0px;left:20px;top:40px; position: absolute;\"");
			buf.append(" onclick=\"$Actions['addUIWin']('tabId='+$('desktopmenu').getAttribute('tabId'));\">");
			buf.append("<span class=\"icon\"><img src=\"" + requestResponse.getContextPath()
					+ "/frame/template/t100/themes/default/images/add_icon.png\"> </span>");
			buf.append("<div class=\"textdesc\">添加<s></s></div>");
			buf.append("</div></div>");
		}
		buf.append("<div id='ui-tab4__' class=\"deskIcon ui-droppable\" style=\"margin-left: 85px;width:300px;\">");
		for (DesktopBean desktopBean : sessionMenuMap.values()) {
			buf.append("<div class=\"desktop_icon ui-draggable ui-droppable titem\" title=\"" + desktopBean.text + "\" rowid=\"" + desktopBean.name
					+ "\" style=\"margin: 0px;left:20px;top:40px;position: absolute;\"");
			buf.append(" onclick=\"$('now_ui_name').value='" + desktopBean.name + "';$desktop.hideFaskBar('" + desktopBean.name + "__');"
					+ desktopBean.action + "\">");
			buf.append("<span class=\"icon\"><img id='ui_" + desktopBean.name + "' src=\"" + requestResponse.getContextPath() + desktopBean.icon
					+ "\"> </span>");
			buf.append("<div class=\"textdesc\">" + (StringUtils.substring(desktopBean.text, 5)) + "<s></s></div>");
			buf.append("</div>");
		}
		buf.append("</div></div>");
		return buf.toString();
	}

	public static String buildGridDesktop(PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		ITableEntityManager tMgr = DesktopUIUtils.application.getDataObjectManager();
		IAccount account = AccountSession.getLogin(requestResponse.getSession());
		if (account == null) {
			return "";
		}

		String oorder = WebUtils.getCookie(requestResponse.request, "oorder");
		if (StringsUtils.isNotBlank1(oorder)) {
			oorder = "," + oorder;
		} else {
			oorder = "";
		}
		IQueryEntitySet<DesktopUIBean> qs = tMgr.query(new ExpressionValue("userId=? order by tabId " + oorder, new Object[] { account.getId() }),
				DesktopUIBean.class);
		DesktopUIBean uiBean = null;
		Map<String, List<DesktopUIBean>> map = new HashMap<String, List<DesktopUIBean>>();
		while ((uiBean = qs.next()) != null) {
			List<DesktopUIBean> list = map.get(uiBean.getTabId());
			if (list == null) {
				list = new ArrayList<DesktopUIBean>();
				map.put(uiBean.getTabId(), list);
			}
			list.add(uiBean);
		}
		Map<String, DesktopBean> sessionMenuMap = DesktopMgr.getDesktopMgr().getPermissionDesktopMap(requestResponse.request);
		int i = 1;
		for (String tab : tabList.keySet()) {
			buf.append("<div class='grid_tab' align='left' style='" + (i == 4 ? "width:24%;border-right: 0px;" : "") + "'>");
			buf.append("<div class='folder_bg folder_bg" + (i++) + "'></div>");
			buf.append("<div class='folderOuter'>");
			buf.append("<div class='folderInner' style='height: 100%; overflow: hidden;'>");
			if (i == 5) {
				for (DesktopBean desktopBean : sessionMenuMap.values()) {
					buf.append("<div class='appButton amg_folder_appbutton' title=\"" + desktopBean.text + "\"");
					buf.append(" onclick=\"$('all_grid_content').toggle();$('now_ui_name').value='" + desktopBean.name + "';$desktop.hideFaskBar('"
							+ desktopBean.name + "__');" + desktopBean.action + "\">");
					buf.append("<div class='appButton_appIcon'><img class='appButton_appIconImg' src=\"" + desktopBean.icon + "\" alt=''></div>");
					buf.append("<div class='appButton_appName'>" + desktopBean.text + "</div>");
					buf.append("</div>");
				}
			} else {
				List<DesktopBean> list1 = DesktopMgr.getDesktopMgr().desktopDefaultMap.get(tab);
				if (list1 != null)
					for (DesktopBean bean : list1) {
						buf.append("<div class='appButton amg_folder_appbutton' title=\"" + bean.text + "\"");
						buf.append(" onclick=\"$('all_grid_content').toggle();$('now_ui_name').value='" + bean.uiName + "';$desktop.hideFaskBar('"
								+ bean.name + "__');" + bean.action + "\">");
						buf.append("<div class='appButton_appIcon'><img class='appButton_appIconImg' src=\"" + requestResponse.getContextPath()
								+ bean.icon + "\" alt=''></div>");
						buf.append("<div class='appButton_appName'>" + bean.text + "</div>");
						buf.append("</div>");
					}
				List<DesktopUIBean> list = map.get(tab);
				if (list != null)
					for (DesktopUIBean bean : list) {
						DesktopBean desktopBean = DesktopMgr.getDesktopMgr().getDesktopBean(bean.getUiName());
						if (desktopBean != null) {
							if (sessionMenuMap.containsKey(desktopBean.name)) {
								buf.append("<div class='appButton amg_folder_appbutton' title=\"" + desktopBean.text + "\"");
								buf.append(" onclick=\"$('all_grid_content').toggle();$('now_ui_name').value='" + bean.getUiName()
										+ "';$desktop.hideFaskBar('" + desktopBean.name + "__');" + desktopBean.action + "\">");
								buf.append("<div class='appButton_appIcon'><img class='appButton_appIconImg' src=\""
										+ requestResponse.getContextPath() + desktopBean.icon + "\" alt=''></div>");
								buf.append("<div class='appButton_appName'>" + desktopBean.text + "</div>");
								buf.append("</div>");
							}
						}
					}
			}
			buf.append("</div></div></div>");
		}
		return buf.toString();
	}
}
