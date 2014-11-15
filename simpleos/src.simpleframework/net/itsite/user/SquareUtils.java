package net.itsite.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteOrganizationApplicationModule.AccountExt;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountViewLog;
import net.simpleframework.util.DateUtils.TimeDistance;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 广场
 * @author 李岩飞
 *
 */
public class SquareUtils {
	public static IDataObjectQuery<Map<String, Object>> querySquare(final PageRequestResponse requestResponse) {
		final String type = requestResponse.getRequestParameter("type");
		final StringBuffer sql = new StringBuffer();
		final StringBuffer where = new StringBuffer();
		final List<Object> ol = new ArrayList<Object>();
		if ("views".equals(type)) {
			final ITableEntityManager temgr = OrgUtils.getTableEntityManager(AccountViewLog.class);
			sql.append("select accountId,count(*) as ds from ").append(temgr.getTablename());
			sql.append(" where lastupdate>=?");
			ol.add(net.simpleframework.util.DateUtils.getTimeCalendar(TimeDistance.month).getTime());
			sql.append(where);
			sql.append(" group by accountId order by ds desc");
			return temgr.query(new SQLValue(sql.toString(), ol.toArray()));
		} else if ("activity".equals(type)) {
			final ITableEntityManager temgr = OrgUtils.getTableEntityManager(AccountExt.class);
			sql.append("select id as accountId,sum(logintimes) as ds from ").append(temgr.getTablename());
			sql.append(" where lastlogindate>=?");
			ol.add(net.simpleframework.util.DateUtils.getTimeCalendar(TimeDistance.week).getTime());
			sql.append(where);
			sql.append(" group by accountId order by ds desc  ");
			return temgr.query(new SQLValue(sql.toString(), ol.toArray()));
		} else if ("news".equals(type)) {
			final ITableEntityManager temgr = OrgUtils.getTableEntityManager(AccountExt.class);
			sql.append("select id as accountId from ").append(temgr.getTablename());
			sql.append(" order by createDate desc");
			return temgr.query(new SQLValue(sql.toString(), ol.toArray()));
		}
		return null;
	}
}
