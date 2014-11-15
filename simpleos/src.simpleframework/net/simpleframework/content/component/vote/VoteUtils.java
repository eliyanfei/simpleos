package net.simpleframework.content.component.vote;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class VoteUtils {
	public static final String BEAN_ID = "vote_@bid";

	public static final String VOTE_GROUP_ID = "__vote_groupId";

	public static final String VOTE_ITEM_ID = "__vote_itemId";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static Table table_vote = new Table("simple_vote");

	public static Table table_vote_group = new Table("simple_vote_group");

	public static Table table_vote_item = new Table("simple_vote_item");

	public static Table table_vote_result = new Table("simple_vote_result");

	public static void putTables(final Map<Class<?>, Table> tables) {
		tables.put(Vote.class, table_vote);
		tables.put(VoteItemGroup.class, table_vote_group);
		tables.put(VoteItem.class, table_vote_item);
		tables.put(VoteResult.class, table_vote_result);
	}

	public static Class<?>[] VOTE_CLASS = new Class[] { Vote.class, VoteItem.class,
			VoteItemGroup.class, VoteResult.class };

	public static <T extends IDataObjectBean> SQLValue[] getDeleteSQLs(
			final ComponentParameter compParameter, final Class<T> beanClazz, final Object[] ids) {
		final IDbComponentHandle cHandle = (IDbComponentHandle) compParameter.getComponentHandle();
		final Map<Class<?>, String> tablenames = new HashMap<Class<?>, String>();
		for (final Class<?> clazz : VOTE_CLASS) {
			tablenames.put(clazz, cHandle.getTableEntityManager(compParameter, clazz).getTablename());
		}
		return getDeleteSQLs(tablenames, beanClazz, ids);
	}

	public static <T extends IDataObjectBean> SQLValue[] getDeleteSQLs(
			final Map<Class<?>, String> tablenames, final Class<T> beanClazz, final Object[] ids) {
		int length;
		if (ids != null && (length = ids.length) > 0) {
			final String simple_vote_result = tablenames.get(VoteResult.class);
			final String simple_vote_item = tablenames.get(VoteItem.class);
			final String simple_vote_group = tablenames.get(VoteItemGroup.class);
			final String simple_vote = tablenames.get(Vote.class);
			if (Vote.class.isAssignableFrom(beanClazz)) {
				final String sql1 = "delete from " + simple_vote_result + " where itemid in "
						+ "(select id from " + simple_vote_item + " where groupid in (select id from "
						+ simple_vote_group + " where " + DbUtils.getIdsSQLParam("voteid", length) + "))";
				final String sql2 = "delete from " + simple_vote_item
						+ " where groupid in (select id from " + simple_vote_group + " where "
						+ DbUtils.getIdsSQLParam("voteid", length) + ")";
				final String sql3 = "delete from " + simple_vote_group + " where "
						+ DbUtils.getIdsSQLParam("voteid", length);
				final String sql4 = "delete from " + simple_vote + " where "
						+ DbUtils.getIdsSQLParam("id", length);
				return new SQLValue[] { new SQLValue(sql1, ids), new SQLValue(sql2, ids),
						new SQLValue(sql3, ids), new SQLValue(sql4, ids) };
			} else if (VoteItemGroup.class.isAssignableFrom(beanClazz)) {
				final String sql1 = "delete from " + simple_vote_result + " where itemid in "
						+ "(select id from " + simple_vote_item + " where "
						+ DbUtils.getIdsSQLParam("groupid", length) + ")";
				final String sql2 = "delete from " + simple_vote_item + " where "
						+ DbUtils.getIdsSQLParam("groupid", length);
				final String sql3 = "delete from " + simple_vote_group + " where "
						+ DbUtils.getIdsSQLParam("id", length);
				return new SQLValue[] { new SQLValue(sql1, ids), new SQLValue(sql2, ids),
						new SQLValue(sql3, ids) };
			} else if (VoteItem.class.isAssignableFrom(beanClazz)) {
				final String sql1 = "delete from " + simple_vote_result + " where "
						+ DbUtils.getIdsSQLParam("itemid", length);
				final String sql2 = "delete from " + simple_vote_item + " where "
						+ DbUtils.getIdsSQLParam("id", length);
				return new SQLValue[] { new SQLValue(sql1, ids), new SQLValue(sql2, ids) };
			}
		}
		return null;
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(VoteRegistry.vote).getResourceHomePath();
	}
}
