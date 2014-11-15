package net.simpleframework.util.db;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Logger;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.EOrder;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class SQLParserUtils {
	static Logger logger = ALoggerAware.getLogger(SQLParserUtils.class);

	public static String wrapCountColumn(final String sql) {
		final StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from (").append(sql).append(") t_count");
		return sb.toString();
	}

	private static Map<String, String> mSQL = new ConcurrentHashMap<String, String>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static String replaceCountColumn(final String sql) {
		final String key = sql + ";count";
		String nsql = mSQL.get(key);
		if (nsql != null) {
			return nsql;
		}
		try {
			final PlainSelect select = new CCJSqlParser(new StringReader(sql)).PlainSelect();
			if (select.getGroupByColumnReferences() != null || select.getDistinct() != null
					|| isColumnAlias(select)) {
				nsql = wrapCountColumn(sql);
			} else {
				final List items = select.getSelectItems();
				items.clear();
				items.add(new SelectItem() {
					@Override
					public void accept(final SelectItemVisitor selectItemVisitor) {
					}

					@Override
					public String toString() {
						return "count(*)";
					}
				});
				final List<?> orders = select.getOrderByElements();
				if (orders != null) {
					orders.clear();
				}
				nsql = select.toString();
			}
		} catch (final Throwable e) {
			warn(e, sql);
			nsql = wrapCountColumn(sql);
		}
		mSQL.put(key, nsql);
		return nsql;
	}

	public static String wrapOrderBy(final String sql, final Column... orderBy) {
		if (orderBy == null) {
			return sql;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("select * from (").append(sql).append(") t_order_by order by ")
				.append(stringOrderby(orderBy));
		return sb.toString();
	}

	private static String stringOrderby(final Column... orderBy) {
		final StringBuilder sb = new StringBuilder();
		int i = 0;
		for (final Column column : orderBy) {
			if (column.getOrder() == EOrder.normal) {
				continue;
			}
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append(column).append(" ").append(column.getOrder());
		}
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static String addOrderBy(final String sql, final Column... orderBy) {
		if (orderBy == null) {
			return sql;
		}
		final String key = sql + ";" + stringOrderby(orderBy);
		String nsql = mSQL.get(key);
		if (nsql != null) {
			return nsql;
		}
		try {
			final PlainSelect select = new CCJSqlParser(new StringReader(sql)).PlainSelect();
			List orders = select.getOrderByElements();
			if (orders == null) {
				select.setOrderByElements(orders = new ArrayList());
			}
			for (int i = orderBy.length - 1; i >= 0; i--) {
				final Column column = orderBy[i];
				OrderByElement element = null;
				for (final Object o : orders) {
					final OrderByElement element2 = (OrderByElement) o;
					final net.sf.jsqlparser.schema.Column col = (net.sf.jsqlparser.schema.Column) element2
							.getExpression();
					if (column.toString().equals(col.getWholeColumnName())) {
						element = element2;
						break;
					}
				}
				if (element == null) {
					element = new OrderByElement();
				} else {
					orders.remove(element);
				}
				final Table table = column.getTable();
				element.setExpression(new net.sf.jsqlparser.schema.Column(
						new net.sf.jsqlparser.schema.Table(null, table != null ? table.getName() : null),
						column.getColumnSqlName()));
				element.setAsc(column.getOrder() == EOrder.asc);
				orders.add(0, element);
			}
			nsql = select.toString();
		} catch (final Throwable e) {
			warn(e, sql);
			nsql = wrapOrderBy(sql, orderBy);
		}
		mSQL.put(key, nsql);
		return nsql;
	}

	// sum, avg, max, min
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static String replaceFunctionColumn(final String sql, final String functionColumn) {
		try {
			final PlainSelect select = new CCJSqlParser(new StringReader(sql)).PlainSelect();
			if (isColumnAlias(select)) {
				return wrapFunctionColumn(sql, functionColumn);
			}
			final List items = select.getSelectItems();
			items.clear();
			items.add(new SelectItem() {
				@Override
				public void accept(final SelectItemVisitor selectItemVisitor) {
				}

				@Override
				public String toString() {
					return functionColumn;
				}
			});
			return select.toString();
		} catch (final ParseException e) {
			warn(e, sql);
			return wrapFunctionColumn(sql, functionColumn);
		}
	}

	static String wrapFunctionColumn(final String sql, final String functionColumn) {
		final StringBuilder sb = new StringBuilder();
		sb.append("select ").append(functionColumn).append(" from ").append("(").append(sql)
				.append(") t_function");
		return sb.toString();
	}

	private static boolean isColumnAlias(final PlainSelect select) {
		final Expression where = select.getWhere();
		for (final Object column : select.getSelectItems()) {
			if (column instanceof SelectExpressionItem) {
				final String alias = ((SelectExpressionItem) column).getAlias();
				if (StringUtils.hasText(alias) && where != null
						&& where.toString().toLowerCase().contains(alias.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	public static String wrapCondition(final String sql, final String condition) {
		if (!StringUtils.hasText(condition)) {
			return sql;
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("select * from (").append(sql);
		sb.append(") t_condition where ").append(condition);
		return sb.toString();
	}

	static String addCondition(final String sql, final String condition) {
		if (!StringUtils.hasText(condition)) {
			return sql;
		}
		try {
			final PlainSelect select = new CCJSqlParser(new StringReader(sql)).PlainSelect();
			final Expression expression = new Expression() {
				@Override
				public void accept(final ExpressionVisitor expressionVisitor) {
				}

				@Override
				public String toString() {
					return condition;
				}
			};
			final Expression where = select.getWhere();
			if (where != null) {
				select.setWhere(new AndExpression(where, expression));
			} else {
				select.setWhere(expression);
			}
			return select.toString();
		} catch (final Throwable e) {
			warn(e, sql);
			return wrapCondition(sql, condition);
		}
	}

	static void warn(final Throwable e, final String sql) {
		logger.warn(e.getMessage() + "\nSQL: [" + sql + "]");
	}

	public static String replaceCountColumn(final DataSource dataSource, final String sql) {
		final String db = DbUtils.getDatabaseMetaData(dataSource).databaseProductName;
		if (db.equals(ISQLConstants.MySQL)) {
			return SQLParserUtils.replaceCountColumn(sql);
		} else {
			final String nsql = SQLParserUtils.wrapCountColumn(sql);
			return nsql;
		}
	}

	public static String replaceColumn(final DataSource dataSource, final String sql,
			final String functionColumn) {
		final String db = DbUtils.getDatabaseMetaData(dataSource).databaseProductName;
		if (db.equals(ISQLConstants.MySQL)) {
			return SQLParserUtils.replaceFunctionColumn(sql, functionColumn);
		} else {
			return SQLParserUtils.wrapFunctionColumn(sql, functionColumn);
		}
	}

	public static String addCondition(final DataSource dataSource, final String sql,
			final String condition) {
		final String db = DbUtils.getDatabaseMetaData(dataSource).databaseProductName;
		if (db.equals(ISQLConstants.MySQL)) {
			return SQLParserUtils.addCondition(sql, condition);
		} else {
			return SQLParserUtils.wrapCondition(sql, condition);
		}
	}

	public static String addOrderBy(final DataSource dataSource, final String sql,
			final Column... orderBy) {
		final String db = DbUtils.getDatabaseMetaData(dataSource).databaseProductName;
		if (db.equals(ISQLConstants.MySQL)) {
			return SQLParserUtils.addOrderBy(sql, orderBy);
		} else {
			return SQLParserUtils.wrapOrderBy(sql, orderBy);
		}
	}
}
