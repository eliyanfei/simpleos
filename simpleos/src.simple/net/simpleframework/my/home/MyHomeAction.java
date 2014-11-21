package net.simpleframework.my.home;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.SessionCache;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyHomeAction extends AbstractAjaxRequestHandle {

	public IForward addTab(final ComponentParameter compParameter) {
		final String tabText = compParameter.getRequestParameter("tab_name");
		final String description = compParameter.getRequestParameter("tab_description");
		HomeTabsBean homeTab = MyHomeUtils.getHomeTab(compParameter.getRequestParameter("tab_id"));
		if (homeTab == null) {
			final IAccount account = AccountSession.getLogin(compParameter.getSession());
			homeTab = MyHomeUtils.insertHomeTab(account.getId(), tabText, false, description);
		} else {
			homeTab.setTabText(tabText);
			homeTab.setDescription(description);
			MyHomeUtils.getTableEntityManager(HomeTabsBean.class).update(homeTab);
		}
		final String url = MyHomeUtils.applicationModule.getTabUrl(compParameter, homeTab);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("url", url);
			}
		});
	}

	public IForward delTab(final ComponentParameter compParameter) {
		final HomeTabsBean homeTab = MyHomeUtils.getHomeTab(compParameter
				.getRequestParameter("tab_id"));
		final HomeTabsBean firstHomeTab = MyHomeUtils.getFirstHomeTab(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (firstHomeTab.getId().equals(homeTab.getId())) {
					json.put("error", LocaleI18n.getMessage("MyHomeAction.0"));
				} else {
					MyHomeUtils.getTableEntityManager(HomeTabsBean.class).deleteTransaction(
							new ExpressionValue("id=?", new Object[] { homeTab.getId() }),
							new TableEntityAdapter() {
								@Override
								public void afterDelete(final ITableEntityManager manager,
										final IDataObjectValue dataObjectValue) {
									MyHomeUtils.getTableEntityManager(HomePortalBean.class).delete(
											new ExpressionValue("id=?", new Object[] { homeTab.getId() }));
								}
							});
					json.put("url", MyHomeUtils.applicationModule.getTabUrl(compParameter, firstHomeTab));
				}
			}
		});
	}

	public IForward resetSpace(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final HomeTabsBean firstHomeTab = MyHomeUtils.getFirstHomeTab(compParameter);
				final ITableEntityManager layoutmgr = MyHomeUtils
						.getTableEntityManager(HomePortalBean.class);
				final HomePortalBean homeLayout = layoutmgr.queryForObjectById(firstHomeTab.getId(),
						HomePortalBean.class);
				try {
					homeLayout.setLayoutLob(new InputStreamReader(getClass().getClassLoader()
							.getResourceAsStream("net/simpleframework/my/home/default_layout.xml"),
							IConstants.UTF8));
				} catch (final UnsupportedEncodingException e) {
				}
				layoutmgr.update(homeLayout);
				SessionCache.remove(compParameter.getSession(), homeLayout.getId());
				json.put("ok", Boolean.TRUE);
			}
		});
	}
}
