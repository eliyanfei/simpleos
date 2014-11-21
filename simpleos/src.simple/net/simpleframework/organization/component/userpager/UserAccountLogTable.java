package net.simpleframework.organization.component.userpager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.AccountLog;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.IAccountRule;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserAccountLogTable extends AbstractDbTablePagerHandle {
	@Override
	public IApplicationModule getApplicationModule() {
		return OrgUtils.applicationModule;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<a onclick=\"$Actions['user_account_log_table']('eventId=&time='); this.up('.user_account_log').down('select').selectedIndex = 0;\">");
			sb.append(LocaleI18n.getMessage("user_account_log.2")).append("</a>");
			final String eventId = compParameter.getRequestParameter("eventId");
			if (StringUtils.hasText(eventId)) {
				final IAccountRule aRule = AccountContext.getAccountRule(eventId);
				if (aRule != null) {
					sb.append(HTMLBuilder.NAV);
					sb.append("<a onclick=\"$Actions['user_account_log_table']('time=');\">");
					sb.append(aRule.getText()).append("</a>");
				}
			}
			final String time = compParameter.getRequestParameter("time");
			if (StringUtils.hasText(time)) {
				if ("day".equals(time)) {
					sb.append(HTMLBuilder.NAV);
					sb.append(LocaleI18n.getMessage("user_account_log.3"));
				} else if ("week".equals(time)) {
					sb.append(HTMLBuilder.NAV);
					sb.append(LocaleI18n.getMessage("user_account_log.4"));
				}
			}
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "eventId");
		putParameter(compParameter, parameters, "time");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}

		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		sql.append("accountId=?");
		al.add(login.getId());
		final String eventId = compParameter.getRequestParameter("eventId");
		if (StringUtils.hasText(eventId)) {
			sql.append(" and eventid=?");
			al.add(eventId);
		}
		final String time = compParameter.getRequestParameter("time");
		if (StringUtils.hasText(time)) {
			sql.append(" and createdate>?");
			al.add(DateUtils.getTimeCalendar(time).getTime());
		}
		return OrgUtils.getTableEntityManager(AccountLog.class).query(
				new ExpressionValue(sql.toString(), al.toArray()), AccountLog.class);
	}

	@Override
	protected Map<Object, Object> getTableRow(final ComponentParameter compParameter,
			final Object dataObject) {
		final AccountLog log = (AccountLog) dataObject;
		final Map<Object, Object> row = new HashMap<Object, Object>();
		final StringBuilder sb = new StringBuilder();
		final IAccountRule aRule = AccountContext.getAccountRule(log.getEventId());
		if (aRule != null) {
			final String module = aRule.getModule();
			if (StringUtils.hasText(module)) {
				sb.append("[").append(module).append("]&nbsp;");
			}
			sb.append("<span style=\"color: green;\">").append(aRule.getText()).append("</span>");
			row.put("eventText", sb.toString());
		}
		row.put("createDate", ConvertUtils.toDateString(log.getCreateDate()));
		row.put("exp", log.getExp());

		final int points = log.getPoints();
		sb.setLength(0);
		if (points < 0) {
			sb.append("<span style=\"color: red;\">");
		}
		sb.append(points);
		if (points < 0) {
			sb.append("</span>");
		}
		row.put("points", sb.toString());
		row.put("reputation", log.getReputation());
		row.put("money", log.getMoney());
		row.put("desc", StringUtils.blank(log.getDescription()));
		return row;
	}
}
