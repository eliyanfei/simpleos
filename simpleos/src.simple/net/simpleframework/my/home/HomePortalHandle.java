package net.simpleframework.my.home;

import java.io.StringReader;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.SessionCache;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.ColumnBean;
import net.simpleframework.web.page.component.ui.portal.DefaultPortalHandle;
import net.simpleframework.web.page.component.ui.portal.PortalBean;
import net.simpleframework.web.page.component.ui.portal.PortalRegistry;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HomePortalHandle extends DefaultPortalHandle {
	@Override
	public Collection<ColumnBean> getPortalColumns(final ComponentParameter compParameter) {
		final ColumnCache columnCache = getColumnCache(compParameter, getHomeTab(compParameter));
		return columnCache != null ? columnCache.columns : null;
	}

	protected ColumnCache getColumnCache(final ComponentParameter compParameter, final HomeTabsBean homeTab) {
		if (homeTab == null) {
			return null;
		}
		final ID tabId = homeTab.getId();
		final ColumnCache columnCache = (ColumnCache) SessionCache.get(compParameter.getSession(), tabId);
		if (columnCache != null) {
			return columnCache;
		}
		final HomePortalBean homeLayout = MyHomeUtils.getTableEntityManager(HomePortalBean.class).queryForObjectById(tabId, HomePortalBean.class);
		final ColumnCache[] columnCacheArr = new ColumnCache[1];
		new AbstractXmlDocument(homeLayout.getLayoutLob()) {
			@Override
			protected void init() throws Exception {
				columnCacheArr[0] = new ColumnCache(getDocument(),
						((PortalRegistry) AbstractComponentRegistry.getRegistry(PortalRegistry.portal)).loadBean(compParameter,
								(PortalBean) compParameter.componentBean, getRoot()));
				SessionCache.put(compParameter.getSession(), tabId, columnCacheArr[0]);
			}
		};
		return columnCacheArr[0];
	}

	@Override
	public void updatePortal(final ComponentParameter compParameter, final Collection<ColumnBean> columns, final boolean draggable) {
		final HomeTabsBean homeTab = getHomeTab(compParameter);
		if (homeTab == null) {
			return;
		}
		final ID tabId = homeTab.getId();
		final HomePortalBean homeLayout = MyHomeUtils.getTableEntityManager(HomePortalBean.class).queryForObjectById(tabId, HomePortalBean.class);
		final ColumnCache columnCache = (ColumnCache) SessionCache.get(compParameter.getSession(), tabId);
		if (columnCache == null) {
			return;
		}
		final Element root = columnCache.document.getRootElement();
		root.clearContent();
		root.addAttribute("draggable", String.valueOf(draggable));
		columnCache.columns = columns;
		for (final ColumnBean column : columns) {
			column.syncElement();
			final Element ele = column.getElement();
			ele.setParent(null);
			root.add(ele);
		}
		homeLayout.setLayoutLob(new StringReader(columnCache.document.asXML()));
		MyHomeUtils.getTableEntityManager(HomePortalBean.class).update(homeLayout);
	}

	private HomeTabsBean getHomeTab(final ComponentParameter compParameter) {
		final String tabId = compParameter.getRequestParameter(IHomeApplicationModule.MYTAB_ID);
		HomeTabsBean homeTab = MyHomeUtils.getHomeTab(tabId);
		if (homeTab == null) {
			homeTab = MyHomeUtils.getFirstHomeTab(compParameter);
		}
		return homeTab;
	}

	public static class ColumnCache {
		public Document document;

		public Collection<ColumnBean> columns;

		public ColumnCache(final Document document, final Collection<ColumnBean> columns) {
			this.document = document;
			this.columns = columns;
		}
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobManager".equals(beanProperty)) {
			return IJob.sj_account_normal;
		} else if ("draggable".equals(beanProperty)) {
			final ColumnCache columnCache = getColumnCache(compParameter, getHomeTab(compParameter));
			if (columnCache == null) {
				return false;
			}
			return ConvertUtils.toBoolean(columnCache.document.getRootElement().attributeValue("draggable"), false);
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, IHomeApplicationModule.MYTAB_ID);
		return parameters;
	}
}
