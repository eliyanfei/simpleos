package net.simpleframework.my.space;

import java.util.ArrayList;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.friends.Friends;
import net.simpleframework.my.friends.FriendsUtils;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SapceLogPagerHandle extends AbstractPagerHandle {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, OrgUtils.um().getUserIdParameterName());
		putParameter(compParameter, parameters, "vtype");
		putParameter(compParameter, parameters, "lt");
		return parameters;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final String vtype = compParameter.getRequestParameter("vtype");
			final StringBuilder sb = new StringBuilder();
			sb.append("<a");
			if (!StringUtils.hasText(vtype)) {
				sb.append(" class=\"a2 nav_arrow\"");
			}
			sb.append(" onclick=\"$Actions['__my_space_log_pager']('vtype=');\">#(SapceLogPagerHandle.0)</a>");
			sb.append(HTMLBuilder.SEP);

			String vvalue = String.valueOf(EFunctionModule.space_log.ordinal());
			sb.append("<a");
			if (vvalue.equals(vtype)) {
				sb.append(" class=\"a2 nav_arrow\"");
			}
			sb.append(" onclick=\"$Actions['__my_space_log_pager']('vtype=");
			sb.append(vvalue).append("');\">#(SapceLogPagerHandle.1)</a>");
			sb.append(HTMLBuilder.SEP);

			vvalue = EFunctionModule.news.ordinal() + ";" + EFunctionModule.news_remark.ordinal();
			sb.append("<a");
			if (vvalue.equals(vtype)) {
				sb.append(" class=\"a2 nav_arrow\"");
			}
			sb.append(" onclick=\"$Actions['__my_space_log_pager']('vtype=").append(vvalue).append("');\">")
					.append(NewsUtils.applicationModule.getApplicationText()).append("</a>");
			sb.append(HTMLBuilder.SEP);

			vvalue = EFunctionModule.blog.ordinal() + ";" + EFunctionModule.blog_remark.ordinal();
			sb.append("<a");
			if (vvalue.equals(vtype)) {
				sb.append(" class=\"a2 nav_arrow\"");
			}
			sb.append(" onclick=\"$Actions['__my_space_log_pager']('vtype=").append(vvalue).append("');\">")
					.append(BlogUtils.applicationModule.getApplicationText()).append("</a>");
			sb.setLength(0);
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager log_mgr = MySpaceUtils.getTableEntityManager(SapceLogBean.class);
		final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
		if (accountAware.isMyAccount(compParameter)) {
			final StringBuilder sql = new StringBuilder();
			final ArrayList<Object> al = new ArrayList<Object>();
			final Object id = AccountSession.getLogin(compParameter.getSession()).getId();
			final String lt = compParameter.getRequestParameter("lt");
			if ("my".equals(lt)) {
				sql.append("userid=?");
				al.add(id);
				sqlVtype(compParameter, sql, al, null);
				sql.append(" order by createdate desc");
				return log_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), SapceLogBean.class);
			} else {
				sql.append("select DISTINCT t.* from ").append(log_mgr.getTablename()).append(" t left join ");
				if ("friends".equals(lt)) {
					sql.append(FriendsUtils.getTableEntityManager(Friends.class).getTablename());
					sql.append(" a on a.friendid");
				} else {
					sql.append(MySpaceUtils.getTableEntityManager(UserAttentionBean.class).getTablename());
					sql.append(" a on a.attentionid");
				}
				sql.append("=t.userid where (t.userid=? or a.userid=?)");
				al.add(id);
				al.add(id);
				sqlVtype(compParameter, sql, al, "t");
				sql.append(" order by t.createdate desc");
				return log_mgr.query(new SQLValue(sql.toString(), al.toArray()), SapceLogBean.class);
			}
		} else {
			final IAccount account = accountAware.getSpecifiedAccount(compParameter);
			if (account != null) {
				final StringBuilder sql = new StringBuilder();
				final ArrayList<Object> al = new ArrayList<Object>();
				sql.append("userid=?");
				al.add(account.getId());
				sqlVtype(compParameter, sql, al, null);
				sql.append(" order by createdate desc");
				return log_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), SapceLogBean.class);
			}
		}
		return null;
	}

	private void sqlVtype(final ComponentParameter compParameter, final StringBuilder sql, final ArrayList<Object> al, final String table) {
		final String[] arr = StringUtils.split(compParameter.getRequestParameter("vtype"), ";");
		if (arr != null && arr.length > 0) {
			sql.append(" and (");
			int i = 0;
			for (final String str : arr) {
				if (i++ > 0) {
					sql.append(" or ");
				}
				if (table != null) {
					sql.append(table).append(".");
				}
				sql.append("refModule=?");
				al.add(str);
			}
			sql.append(")");
		}
	}
}
