package net.simpleframework.applets.attention;

import java.util.ArrayList;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.DateUtils.TimeDistance;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AttentionUtils {
	public static IAttentionApplicationModule applicationModule;

	public static String deployPath;

	public static String getCssPath(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(deployPath).append("css/").append(applicationModule.getSkin(requestResponse));
		return sb.toString();
	}

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static AttentionBean get(final PageRequestResponse requestResponse, final EFunctionModule vtype, final Object attentionId) {
		return applicationModule.get(requestResponse, vtype, attentionId);
	}

	public static AttentionBean insert(final PageRequestResponse requestResponse, final EFunctionModule vtype, final ID attentionId) {
		return applicationModule.insert(requestResponse, vtype, attentionId);
	}

	public static AttentionBean insert(final ID userId, final EFunctionModule vtype, final ID attentionId) {
		return applicationModule.insert(userId, vtype, attentionId);
	}

	public static void delete(final PageRequestResponse requestResponse, final EFunctionModule vtype, final Object attentionId) {
		applicationModule.delete(requestResponse, vtype, attentionId);
	}

	public static void deleteByAttentionId(final PageRequestResponse requestResponse, final EFunctionModule vtype, final Object[] attentionIds) {
		applicationModule.deleteByAttentionId(requestResponse, vtype, attentionIds);
	}

	public static void sentMessage(final PageRequestResponse requestResponse, final EFunctionModule vtype, final ISentCallback callback) {
		applicationModule.sentMessage(requestResponse, vtype, callback);
	}

	private static TabHref createTabHref(final String applicationUrl, final EFunctionModule e) {
		return new TabHref(IWebApplicationModule.Utils.getApplicationModule(e).getApplicationText(), WebUtils.addParameters(applicationUrl,
				IContentPagerHandle._VTYPE + "=" + e.ordinal()));
	}

	public static String tabs(final PageRequestResponse requestResponse) {
		final String applicationUrl = applicationModule.getApplicationUrl(requestResponse);
		return TabsUtils.tabs(requestResponse, createTabHref(applicationUrl, EFunctionModule.bbs),
				createTabHref(applicationUrl, EFunctionModule.blog), createTabHref(applicationUrl, EFunctionModule.news));
	}

	public static IQueryEntitySet<?> queryAttentions(final PageRequestResponse requestResponse, final byte vtype, final TimeDistance timeDistance) {
		return DataObjectManagerUtils.getQueryEntityManager(applicationModule.getApplication()).query(
				new SQLValue("select count(*) as c, attentionid, vtype from " + getTableEntityManager().getTablename()
						+ " where vtype=? and createdate>? group by attentionid order by c desc", new Object[] { vtype,
						DateUtils.getTimeCalendar(timeDistance).getTime() }));
	}

	public static IQueryEntitySet<AttentionBean> queryAttentions(EFunctionModule vtype, ID attentionId) {
		return applicationModule.queryAttentions(vtype, attentionId);
	}

	public static SQLValue attentionSQLValue(final PageRequestResponse requestResponse, final EFunctionModule aModule) {
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		final String tbl = DataObjectManagerUtils.getTableEntityManager(IWebApplicationModule.Utils.getApplicationModule(aModule)).getTablename();
		sql.append("select t.* from ").append(tbl).append(" t right join ").append(getTableEntityManager().getTablename())
				.append(" a on t.id=attentionid where a.accountId=? and a.vtype=?").append(" order by t.ttop desc, t.createdate desc");
		final IAccount login = AccountSession.getLogin(requestResponse.getSession());
		al.add(login != null ? login.getId() : 0);
		al.add(aModule);
		return new SQLValue(sql.toString(), al.toArray());
	}
}
