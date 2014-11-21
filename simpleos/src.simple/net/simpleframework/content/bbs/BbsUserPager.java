package net.simpleframework.content.bbs;

import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountContext;
import net.simpleframework.organization.account.Exp;
import net.simpleframework.organization.component.userpager.UserPagerUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.IPAndCity;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsUserPager extends AbstractDbTablePagerHandle {
	@Override
	public IApplicationModule getApplicationModule() {
		return BbsUtils.applicationModule;
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			sb.append("<a href=\"").append(BbsUtils.applicationModule.getApplicationUrl(compParameter));
			sb.append("\">").append(BbsUtils.applicationModule.getApplicationText()).append("</a>");
			sb.append(HTMLBuilder.NAV).append(LocaleI18n.getMessage("bbs_topic_view.0"));
			wrapNavImage(compParameter, sb);
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final StringBuilder sql = new StringBuilder();
		final String bbs_user = getTableEntityManager(compParameter, BbsUser.class).getTablename();
		final String account_name = OrgUtils.am().tblname();
		sql.append("select a.id,a.topics,a.messages,b.exp,b.lastlogindate,b.lastloginip from ").append(bbs_user).append(" a left join ")
				.append(account_name).append(" b on a.id=b.id");
		// final String orderby = TablePagerUtils.getSortSQL(compParameter,
		// null,
		// null);
		// if (StringUtils.hasText(orderby)) {
		// sql.append(" order by ").append(orderby);
		// }
		return DataObjectManagerUtils.getQueryEntityManager(WebUtils.application).query(sql.toString());
	}

	@Override
	protected Map<Object, Object> getTableRow(final ComponentParameter compParameter, final Object dataObject) {
		final Map<Object, Object> row = super.getTableRow(compParameter, dataObject);
		final Object exp = row.get("EXP");
		if (exp != null) {
			final Exp exp2 = AccountContext.getExp(ConvertUtils.toInt(exp, 0));
			row.put("exp", exp2 + UserPagerUtils.getExpIcon(exp2));
		}
		final Object lastlogindate = row.get("LASTLOGINDATE");
		if (lastlogindate != null) {
			row.put("lastlogindate", DateUtils.getRelativeDate((java.util.Date) lastlogindate));
		}
		final Object lastloginip = row.get("LASTLOGINIP");
		if (lastloginip != null) {
			try {
				row.put("userFrom", IPAndCity.getCity(String.valueOf(lastloginip), true));
			} catch (final Exception e) {
			}
		}
		final Object id = row.get("ID");
		final IUser user = OrgUtils.um().queryForObjectById(id);
		if (user != null) {
			row.put("userText", user.getText());
		}
		return row;
	}
}
