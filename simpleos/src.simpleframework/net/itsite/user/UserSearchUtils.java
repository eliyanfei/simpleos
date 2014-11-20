package net.itsite.user;

import java.util.LinkedHashMap;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 用户搜索
 * @author 李岩飞
 */
public class UserSearchUtils {
	static String c;
	public final static Map<String, ID> dataMap = new LinkedHashMap<String, ID>();

	public static void initDataMap(final PageRequestResponse requestResponse) {
		final IQueryEntitySet<UserSearchCatalog> qs = UserSearchUtils.queryUserSearchCatalog(requestResponse);
		UserSearchCatalog usc;
		dataMap.clear();
		while ((usc = qs.next()) != null) {
			dataMap.put(usc.getText().toLowerCase(), usc.getId());
		}
	}

	public static void createSearch(final PageRequestResponse requestResponse, final EFunctionModule functionModule, final String content) {
		try {
			if (dataMap.isEmpty()) {
				initDataMap(requestResponse);
			}
			if (content != null && !content.equals(c)) {
				final UserSearchBean searchBean = new UserSearchBean();
				searchBean.setFunctionModule(functionModule);
				searchBean.setContent(content);
				final IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
				if (account != null) {
					searchBean.setUserId(account.getId());
				}
				for (String s : UserSearchUtils.dataMap.keySet()) {
					if (content.contains(s)) {
						searchBean.setCatalogId(UserSearchUtils.dataMap.get(s));
						break;
					}
				}
				if (searchBean.getCatalogId() == null)
					searchBean.setCatalogId(UserSearchUtils.dataMap.get("其他"));
				c = content;
				ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, UserSearchBean.class).insertTransaction(searchBean,
						new TableEntityAdapter() {
							@Override
							public void afterInsert(ITableEntityManager manager, Object[] objects) {
								final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule,
										UserSearchCatalog.class);
								UserSearchCatalog searchCatalog = temgr.queryForObjectById(searchBean.getCatalogId(), UserSearchCatalog.class);
								if (searchCatalog != null) {
									searchCatalog.setCounter(searchCatalog.getCounter() + 1);
									searchCatalog.setTday(searchCatalog.getTday() + 1);
									temgr.update(searchCatalog);
								}
							}
						});
			}
		} catch (Exception e) {
		}
	}

	public static IQueryEntitySet<UserSearchCatalog> queryUserSearchCatalog(final PageRequestResponse requestResponse) {
		final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, UserSearchCatalog.class);
		final IQueryEntitySet<UserSearchCatalog> qs = temgr.query(new ExpressionValue("1=1 order by oorder desc"), UserSearchCatalog.class);
		return qs;
	}

	public static IQueryEntitySet<UserSearchBean> queryUserSearch(final PageRequestResponse requestResponse) {
		final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, UserSearchBean.class);
		final IQueryEntitySet<UserSearchBean> qs = temgr.query(new ExpressionValue("catalogId=? order by createDate desc",
				new Object[] { requestResponse.getRequestParameter("cId") }), UserSearchBean.class);
		return qs;
	}

}
