package net.simpleos.backend;

import java.util.Collection;

import net.itsite.ItSiteUtil;
import net.itsite.i.IItSiteApplicationModule;
import net.itsite.impl.AItSiteAppclicationModule;
import net.itsite.impl.PrjColumn;
import net.itsite.impl.PrjColumns;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.ui.window.WindowBean;
import net.simpleos.backend.menu.MenuNavBean;
import net.simpleos.backend.menu.MenuNavUtils;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class BackendUtils {
	public static boolean sysMail = false;
	public static AItSiteAppclicationModule applicationModule = null;

	public static void saveCustom(final String type, final IItSiteApplicationModule module, final ComponentParameter compParameter) {
		PrjColumns columns = module.getPrjColumns(type);
		if (columns == null)
			return;
		String ids = StringsUtils.trimNull(columns.getIds(), "");
		for (final String key : columns.getColumnMap().keySet()) {
			String key1 = key;
			PrjColumn col = columns.getColumnMap().get(key1);
			for (String id : ids.split(",")) {
				id = id.split("=")[0];
				if (ids.split(",").length != 1) {
					key1 = key + "_" + id;
				}
				String value = compParameter.getParameter(key1);
				if (col.isMul()) {
					String[] vs = compParameter.request.getParameterValues(key1);
					if (vs != null)
						value = StringsUtils.join(vs, ",") + ",";
				}
				CustomBean customBean = applicationModule.getBeanByExp(CustomBean.class, "name=? and type=?", new Object[] { key1, type });
				if (customBean == null) {
					customBean = new CustomBean();
					customBean.setType(type);
					customBean.setName(key1);
				}
				if ("sys".equals(type)) {
					sysMail = true;
				}
				ItSiteUtil.attrMap.put(type + "." + key1, StringsUtils.trimNull(value, ""));
				customBean.setValue(StringsUtils.trimNull(value, ""));
				applicationModule.doUpdate(customBean);
			}
		}
	}

	public static void initDesktopDocument(final PageParameter pageParameter) {
		PageDocument document = pageParameter.pageDocument;
		if (document == null)
			return;
		if (document.getDocumentFile().getName().endsWith("tab_1.xml")) {
			Collection<AbstractComponentBean> list = document.getComponentBeans(pageParameter);
			if (list != null && list.size() > 3) {
				return;
			}
			buildMenu(0, document);
		}
	}

	private static void buildMenu(Object parentId, PageDocument document) {
		final IQueryEntitySet<MenuNavBean> qs = MenuNavUtils.appModule.queryBean("mark=0 and parentid=" + parentId + " order by oorder",
				MenuNavBean.class);
		MenuNavBean menu = null;
		while ((menu = qs.next()) != null) {
			AjaxRequestBean ajaxRequestBean = new AjaxRequestBean(document);
			ajaxRequestBean.setName(menu.getName() + "Ajax");
			ajaxRequestBean.setParameters("windowName=" + menu.getName() + "Win");
			ajaxRequestBean.setUrlForward(menu.getUrl());
			document.addComponentBean(ajaxRequestBean);

			WindowBean windowBean = new WindowBean(document, null);
			windowBean.setName(menu.getName() + "Win");
			windowBean.setHeight(600);
			windowBean.setWidth(500);
			windowBean.setMaximize(true);
			windowBean.setContentRef(menu.getName() + "Ajax");
			windowBean.setTitle(menu.getText());
			windowBean.setModal(false);
			windowBean.setJsShownCallback("this.maximize();");
			document.addComponentBean(windowBean);

			buildMenu(menu.getId(), document);
		}
	}

}
