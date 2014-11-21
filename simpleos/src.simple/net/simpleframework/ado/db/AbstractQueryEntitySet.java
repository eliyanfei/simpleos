package net.simpleframework.ado.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.core.ado.AbstractDataObjectQuery;
import net.simpleframework.core.ado.IDataObjectListener;
import net.simpleframework.core.ado.IDataObjectQueryListener;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.SQLParserUtils;

import org.apache.commons.collections.map.LRUMap;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractQueryEntitySet<T> extends AbstractDataObjectQuery<T> implements IQueryEntitySet<T> {
	private final IEntityManager entityManager;

	private SQLValue sqlValue;

	private QueryDialect queryDialect;

	private int fetchSize = -1;

	private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;

	private Map<Integer, T> dataCache;

	public AbstractQueryEntitySet(final IEntityManager entityManager, final SQLValue sqlValue) {
		this.entityManager = entityManager;
		this.sqlValue = sqlValue;
	}

	public AbstractQueryEntitySet(final IEntityManager entityManager, final String sql, final Object[] values) {
		this(entityManager, new SQLValue(sql, values));
	}

	public void setSqlValue(SQLValue sqlValue) {
		if (sqlValue.isAloneLimit())
			this.sqlValue = sqlValue;
	}

	@Override
	public IEntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public void setFetchSize(final int fetchSize) {
		if (this.fetchSize != fetchSize) {
			this.fetchSize = fetchSize;
			dataCache = null;
		}
	}

	@Override
	public int getFetchSize() {
		if (fetchSize < 0) {
			setFetchSize(50);
		}
		return fetchSize;
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, T> getDataCache() {
		if (dataCache == null) {
			dataCache = Collections.synchronizedMap(new LRUMap(getFetchSize() * 5));
		}
		return dataCache;
	}

	@Override
	public void reset() {
		super.reset();
		dataCache = null;
	}

	@Override
	public int getCount() {
		if (count < 0) {
			Object[] values = null;
			String sql = null;
			final IDataObjectValue countSQL = getQueryDialect().getCountSQL();
			if (countSQL != null) {
				values = ((SQLValue) countSQL).getValues();
				sql = ((SQLValue) countSQL).getSql();
			}

			final IEntityManager mgr = getEntityManager();
			if (!StringUtils.hasText(sql)) {
				sql = SQLParserUtils.replaceCountColumn(mgr.getDataSource(), sqlValue.getSql());
				if (values == null || values.length == 0) {
					values = sqlValue.getValues();
				}
			}

			count = mgr.executeQuery(new SQLValue(sql, values), new IQueryExtractor<Integer>() {
				@Override
				public Integer extractData(final ResultSet rs) throws SQLException {
					return rs.next() ? rs.getInt(1) : 0;
				}
			});
		}
		return count;
	}

	public abstract T mapRow(final ResultSet rs, int rowNum) throws SQLException;

	private ResultSet rs;

	@Override
	public void close() {
		if (rs != null) {
			try {
				DataSourceUtils.releaseConnection(rs.getStatement().getConnection(), getEntityManager().getDataSource());
				JdbcUtils.closeStatement(rs.getStatement());
				JdbcUtils.closeResultSet(rs);
			} catch (final SQLException e) {
			} finally {
				rs = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		T bean = null;
		i++;
		final int fetchSize = getFetchSize();
		if (fetchSize <= 0) {
			final DataSource dataSource = getEntityManager().getDataSource();
			try {
				if (i == 0 && rs == null) {
					final PreparedStatement ps = DataSourceUtils.doGetConnection(dataSource).prepareStatement(sqlValue.getSql(), getResultSetType(),
							ResultSet.CONCUR_READ_ONLY);
					Object[] values = sqlValue.getValues();
					if (values != null) {
						values = ResultSetUtils.getParameterValues(dataSource, values);
						for (int col = 0; col < values.length; col++) {
							StatementCreatorUtils.setParameterValue(ps, col + 1, JdbcUtils.TYPE_UNKNOWN, values[col]);
						}
					}
					rs = ps.executeQuery();
				}
				if (rs != null) {
					if (rs.next()) {
						bean = mapRow(rs, i);
					} else {
						close();
					}
				}
			} catch (final SQLException e) {
				final SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
				throw translator.translate("next", sqlValue.getSql(), e);
			}
		} else if (i < getCount() && i >= 0) {
			final Map<Integer, T> dataCache = getDataCache();
			bean = dataCache.get(i);
			if (bean == null) {
				final String sql = sqlValue.getSql();
//				long s = System.currentTimeMillis();
				final String lsql = getQueryDialect().getNativeSQLValue(sql, sqlValue.isAloneLimit() ? -1 : i, fetchSize);
//				System.out.println(lsql);
				final boolean absolute = lsql.equals(sql);
				final IQueryExtractor<T> extractor = new IQueryExtractor<T>() {
					@Override
					public T extractData(final ResultSet rs) throws SQLException {
						if (absolute && i > 0) {
							rs.getStatement().setFetchSize(fetchSize);
							rs.absolute(i);
						}
						int j = -1;
						T first = null;
						while (rs.next()) {
							final int k = i + ++j;
							if (dataCache.containsKey(k)) {
								break;
							}
							final T row = mapRow(rs, j);
							if (j == 0) {
								first = row;
							}
							dataCache.put(k, row);
							// 在oracle测试中
							if (j == fetchSize - 1) {
								break;
							}
						}
						return first;
					}
				};
				bean = getEntityManager().executeQuery(new SQLValue(lsql, sqlValue.getValues()), extractor, getResultSetType());
//				System.out.println(System.currentTimeMillis() - s);
			}
		}
		if (bean != null) {
			pIndex++;
		} else {
			pIndex = -1;
		}
		final boolean pageEnd = (pIndex + 1) == (fetchSize == 0 ? getCount() : fetchSize);
		for (final IDataObjectListener listener : getListeners()) {
			((IDataObjectQueryListener<T>) listener).next(this, bean, pIndex, pageEnd);
		}
		if (pageEnd) {
			pIndex = -1;
		}
		return bean;
	}

	private int pIndex = -1;

	@Override
	public SQLValue getSqlValue() {
		return sqlValue;
	}

	@Override
	public QueryDialect getQueryDialect() {
		if (queryDialect == null) {
			setQueryDialect(new QueryDialect());
		}
		return queryDialect;
	}

	@Override
	public void setQueryDialect(final QueryDialect queryDialect) {
		this.queryDialect = queryDialect;
		this.queryDialect.qs = this;
	}

	@Override
	public int getResultSetType() {
		return resultSetType;
	}

	@Override
	public void setResultSetType(final int resultSetType) {
		this.resultSetType = resultSetType;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}

	public Object doResultSetMetaData(final ResultSetMetaDataCallback callback) {
		final IEntityManager mgr = getEntityManager();
		final String sql = SQLParserUtils.addCondition(mgr.getDataSource(), sqlValue.getSql(), "1 = 2");
		return mgr.executeQuery(new SQLValue(sql, sqlValue.getValues()), new IQueryExtractor<Object>() {
			@Override
			public Object extractData(final ResultSet rs) throws SQLException {
				return callback.doResultSetMetaData(rs.getMetaData());
			}
		});
	}

	public static interface ResultSetMetaDataCallback {

		Object doResultSetMetaData(ResultSetMetaData metaData) throws SQLException;
	}
}
