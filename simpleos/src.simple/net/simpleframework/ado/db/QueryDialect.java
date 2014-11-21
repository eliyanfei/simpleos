package net.simpleframework.ado.db;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.util.db.DbUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class QueryDialect {
	IQueryEntitySet<?> qs;

	private SQLValue countSQL;

	public void setCountSQL(final IDataObjectValue countSQL) {
		final Class<?> c = countSQL.getClass();
		if (SQLValue.class.equals(c)) {
			this.countSQL = (SQLValue) countSQL;
		} else if (ExpressionValue.class.equals(c)) {
			Object mgr;
			if ((mgr = qs.getEntityManager()) instanceof ITableEntityManager) {
				final ExpressionValue ev = (ExpressionValue) countSQL;
				final StringBuilder sb = new StringBuilder();
				sb.append("select count(*) from ").append(((ITableEntityManager) mgr).getTablename())
						.append(" where ").append(ev.getExpression());
				this.countSQL = new SQLValue(sb.toString(), ev.getValues());
			}
		}
	}

	public SQLValue getCountSQL() {
		return countSQL;
	}

	public String getNativeSQLValue(final String sql, final int i, final int fetchSize) {
		return DbUtils.getLocSelectSQL(qs.getEntityManager().getDataSource(), sql, i, fetchSize);
	}
}
