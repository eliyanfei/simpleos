package net.itsite.user;

import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.AbstractMgrToolsAction;
import net.simpleframework.content.IContentApplicationModule;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;

public class UserSearchAjax extends AbstractMgrToolsAction {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward statRebuild(final ComponentParameter compParameter) {
		doStatRebuild();
		UserSearchUtils.initDataMap(compParameter);
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				json.put("info", LocaleI18n.getMessage("manager_tools.3"));
			}
		});
	}

	@Override
	protected void doStatRebuild() {
		final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, UserSearchCatalog.class);
		temgr.execute(new SQLValue("update it_user_search_catalog usc set usc.counter=(select count(*) from it_user_search where catalogId=usc.id)"));
	}

	@Override
	public IForward indexRebuild(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, UserSearchBean.class);
				final IQueryEntitySet<UserSearchBean> beanqs = temgr.query(new ExpressionValue("1=1"), UserSearchBean.class);
				UserSearchBean us;
				while ((us = beanqs.next()) != null) {
					for (String s : UserSearchUtils.dataMap.keySet()) {
						if (us.getContent().toLowerCase().contains(s)) {
							us.setCatalogId(UserSearchUtils.dataMap.get(s));
							break;
						}
					}
					if (us.getCatalogId() == null || us.getCatalogId().equals2(0))
						us.setCatalogId(UserSearchUtils.dataMap.get("其他"));
					temgr.update(us);
				}
				doStatRebuild();
				json.put("info", LocaleI18n.getMessage("manager_tools.6"));
			}
		});
	}

	@Override
	public IContentApplicationModule getApplicationModule() {
		return null;
	}

}
