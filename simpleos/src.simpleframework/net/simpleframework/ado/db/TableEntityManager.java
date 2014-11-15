package net.simpleframework.ado.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.event.ITableEntityListener;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.IDataObjectListener;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.bean.IOrderBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.util.db.ISQLConstants;

import org.springframework.jdbc.support.incrementer.DB2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.SqlServerMaxValueIncrementer;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TableEntityManager extends AbstractEntityManager implements ITableEntityManager {

	private final Table table;

	public TableEntityManager(final DataSource dataSource, final Table table) {
		super(dataSource);
		this.table = table;
	}

	@Override
	public Table getTable() {
		return table;
	}

	@Override
	public String getTablename() {
		return getTable().getName();
	}

	/* select for map */

	@Override
	public Map<String, Object> queryForMap(final IDataObjectValue dataObjectValue) {
		return queryForMap(null, dataObjectValue);
	}

	@Override
	public Map<String, Object> queryForMap(final Object[] columns, final IDataObjectValue dataObjectValue) {
		return createQueryMap(columns, createSQLValue(columns, dataObjectValue));
	}

	@Override
	public IQueryEntitySet<Map<String, Object>> query(final IDataObjectValue dataObjectValue) {
		return query(null, dataObjectValue);
	}

	protected SQLValue createSQLValue(final Object[] columns, final IDataObjectValue dataObjectValue) {
		if (dataObjectValue != null) {
			final Class<?> clazz = dataObjectValue.getClass();
			if (clazz.equals(SQLValue.class)) {
				return (SQLValue) dataObjectValue;
			} else if (clazz.equals(UniqueValue.class)) {
				return new SQLValue(SQLBuilder.getSelectUniqueSQL(getTable(), columns), dataObjectValue != null ? dataObjectValue.getValues() : null);
			} else if (clazz.equals(ExpressionValue.class)) {
				return new SQLValue(SQLBuilder.getSelectExpressionSQL(getTable(), columns, ((ExpressionValue) dataObjectValue).getExpression()),
						dataObjectValue != null ? dataObjectValue.getValues() : null);
			}
		}
		return new SQLValue(SQLBuilder.getSelectExpressionSQL(getTable(), columns, null));
	}

	protected String getSelectUniqueSQL(final Object[] columns) {
		return SQLBuilder.getSelectUniqueSQL(getTable(), columns);
	}

	@Override
	public IQueryEntitySet<Map<String, Object>> query(final Object[] columns, final IDataObjectValue dataObjectValue) {
		return createQueryEntitySet(columns, createSQLValue(columns, dataObjectValue));
	}

	/* select for object */

	@Override
	public <T> T queryForObject(final IDataObjectValue dataObjectValue, final Class<T> beanClass) {
		return queryForObject(null, dataObjectValue, beanClass);
	}

	@Override
	public <T> T queryForObjectById(final Object id, final Class<T> beanClass) throws DataObjectException {
		if (id == null) {
			return null;
		}
		return queryForObject(new UniqueValue(id instanceof ID ? ((ID) id).getValue() : id), beanClass);
	}

	@Override
	public <T> T queryForObject(final Object[] columns, final IDataObjectValue dataObjectValue, final Class<T> beanClass) {
		return createQueryObject(columns, createSQLValue(columns, dataObjectValue), beanClass);
	}

	@Override
	public <T> T queryForObjectById(final Object[] columns, final Object id, final Class<T> beanClass) throws DataObjectException {
		Object id2;
		if (id instanceof ID) {
			id2 = ((ID) id).getValue();
		} else if (id instanceof IIdBeanAware) {
			id2 = ((IIdBeanAware) id).getId().getValue();
		} else {
			id2 = id;
		}
		return queryForObject(columns, new UniqueValue(id2), beanClass);
	}

	@Override
	public <T> IQueryEntitySet<T> query(final IDataObjectValue ev, final Class<T> beanClass) {
		final IQueryEntitySet<T> qs = query(null, ev, beanClass);
		reset();
		return qs;
	}

	@Override
	public <T> IQueryEntitySet<T> query(final Object[] columns, final IDataObjectValue dataObjectValue, final Class<T> beanClass) {
		return createQueryEntitySet(columns, createSQLValue(columns, dataObjectValue), beanClass);
	}

	/* delete */
	@Override
	public int delete(final IDataObjectValue dataObjectValue) {
		return delete(dataObjectValue, null);
	}

	protected int delete(final IDataObjectValue dataObjectValue, final ITableEntityListener l) {
		if (dataObjectValue == null) {
			return 0;
		}
		SQLValue sqlValue = null;
		final Class<?> clazz = dataObjectValue.getClass();
		if (clazz.equals(SQLValue.class)) {
			sqlValue = (SQLValue) dataObjectValue;
		} else if (clazz.equals(UniqueValue.class)) {
			sqlValue = new SQLValue(SQLBuilder.getDeleteUniqueSQL(getTable()), dataObjectValue.getValues());
		} else if (clazz.equals(ExpressionValue.class)) {
			sqlValue = new SQLValue(SQLBuilder.getDeleteExpressionSQL(getTable(), ((ExpressionValue) dataObjectValue).getExpression()),
					dataObjectValue.getValues());
		}
		if (sqlValue == null) {
			return 0;
		}
		if (l != null) {
			l.beforeDelete(this, dataObjectValue);
		}
		for (final IDataObjectListener listener : getListeners()) {
			((ITableEntityListener) listener).beforeDelete(this, dataObjectValue);
		}
		final int ret = executeUpdate(sqlValue);
		if (l != null) {
			l.afterDelete(this, dataObjectValue);
		}
		for (final IDataObjectListener listener : getListeners()) {
			((ITableEntityListener) listener).afterDelete(this, dataObjectValue);
		}
		return ret;
	}

	@Override
	public int deleteTransaction(final IDataObjectValue dataObjectValue, final ITableEntityListener l) {
		return getTransactionTemplate().execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(final TransactionStatus status) {
				return delete(dataObjectValue, l);
			}
		});
	}

	@Override
	public int deleteTransaction(final IDataObjectValue dataObjectValue) {
		return deleteTransaction(dataObjectValue, null);
	}

	/* insert */

	@Override
	public int insert(final Object object) {
		return insert(new Object[] { object });
	}

	@Override
	public int insertTransaction(final Object object, final ITableEntityListener l) {
		return insertTransaction(new Object[] { object }, l);
	}

	@Override
	public int insertTransaction(final Object object) {
		return insertTransaction(object, null);
	}

	@Override
	public int insertTransaction(final Object[] objects) {
		return insertTransaction(objects, null);
	}

	@Override
	public int insertTransaction(final Object[] objects, final ITableEntityListener l) {
		return getTransactionTemplate().execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(final TransactionStatus status) {
				return insert(objects, l);
			}
		});
	}

	@Override
	public int insert(final Object[] objects) {
		return insert(objects, null);
	}

	protected int insert(Object[] objects, final ITableEntityListener l) {
		objects = LangUtils.removeDuplicatesAndNulls(objects);
		if (objects == null) {
			return 0;
		}

		if (l != null) {
			l.beforeInsert(this, objects);
		}
		for (final IDataObjectListener listener : getListeners()) {
			((ITableEntityListener) listener).beforeInsert(this, objects);
		}

		int ret = 0;
		for (final Object object : objects) {
			if (object instanceof IIdBeanAware) {
				final IIdBeanAware idBean = (IIdBeanAware) object;
				if (idBean.getId() == null) {
					idBean.setId(nextId(IEntityBeanAware.Utils.getColumnName(idBean, "id")));
				}
				if (idBean instanceof IOrderBeanAware) {
					final IOrderBeanAware oBean = (IOrderBeanAware) object;
					final Object vObject = idBean.getId().getValue();
					if (oBean.getOorder() == 0 && vObject instanceof Number) {
						oBean.setOorder(((Number) vObject).longValue());
					}
				}
			}
			final SQLValue sqlValue = SQLBuilder.getInsertSQLValue(table, object);
			if (sqlValue != null) {
				ret += executeUpdate(sqlValue);
			}
		}

		if (l != null) {
			l.afterInsert(this, objects);
		}
		for (final IDataObjectListener listener : getListeners()) {
			((ITableEntityListener) listener).afterInsert(this, objects);
		}
		return ret;
	}

	/* update */

	@Override
	public int update(final Object[] columns, final Object object) {
		return update(columns, new Object[] { object });
	}

	@Override
	public int update(final Object object) {
		return update((Object[]) null, object);
	}

	@Override
	public int update(final Object[] objects) {
		return update((Object[]) null, objects);
	}

	@Override
	public int update(final Object[] columns, final Object[] objects) {
		return update(columns, objects, null);
	}

	protected int update(final Object[] columns, Object[] objects, final ITableEntityListener l) {
		objects = LangUtils.removeDuplicatesAndNulls(objects);
		if (objects == null) {
			return 0;
		}
		if (l != null) {
			l.beforeUpdate(this, objects);
		}
		for (final IDataObjectListener listener : getListeners()) {
			((ITableEntityListener) listener).beforeUpdate(this, objects);
		}
		int ret = 0;
		for (final Object object : objects) {
			final SQLValue sqlValue = SQLBuilder.getUpdateSQLValue(table, columns, object);
			if (sqlValue != null) {
				ret += executeUpdate(sqlValue);
			}
		}
		if (l != null) {
			l.afterUpdate(this, objects);
		}
		for (final IDataObjectListener listener : getListeners()) {
			((ITableEntityListener) listener).afterUpdate(this, objects);
		}
		return ret;
	}

	@Override
	public int updateTransaction(final Object[] columns, final Object[] objects, final ITableEntityListener l) {
		return getTransactionTemplate().execute(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(final TransactionStatus status) {
				return update(columns, objects, l);
			}
		});
	}

	@Override
	public int updateTransaction(final Object[] columns, final Object[] objects) {
		return updateTransaction(columns, objects, null);
	}

	@Override
	public int updateTransaction(final Object[] columns, final Object object, final ITableEntityListener l) {
		return updateTransaction(columns, new Object[] { object }, l);
	}

	@Override
	public int updateTransaction(final Object[] columns, final Object object) {
		return updateTransaction(columns, object, null);
	}

	@Override
	public int updateTransaction(final Object[] objects, final ITableEntityListener l) {
		return updateTransaction((Object[]) null, objects, l);
	}

	@Override
	public int updateTransaction(final Object[] objects) {
		return updateTransaction(objects, (ITableEntityListener) null);
	}

	@Override
	public int updateTransaction(final Object object, final ITableEntityListener l) {
		return updateTransaction((Object[]) null, object, l);
	}

	@Override
	public int updateTransaction(final Object object) {
		return updateTransaction(object, (ITableEntityListener) null);
	}

	@Override
	public int getCount(final IDataObjectValue dataObjectValue) {
		return query(dataObjectValue).getCount();
	}

	@Override
	public int getSum(final String column, final IDataObjectValue dataObjectValue) {
		SQLValue sqlValue;
		if (dataObjectValue instanceof SQLValue) {
			sqlValue = (SQLValue) dataObjectValue;
		} else {
			final StringBuilder sql = new StringBuilder();
			sql.append("select sum(").append(column).append(") from ").append(getTable().getName());
			if (dataObjectValue != null) {
				final Class<?> clazz = dataObjectValue.getClass();
				if (clazz.equals(UniqueValue.class)) {
					sql.append(" where ");
					SQLBuilder.buildUniqueColumns(sql, getTable());
				} else if (clazz.equals(ExpressionValue.class)) {
					sql.append(" where ").append(((ExpressionValue) dataObjectValue).getExpression());
				}
			}
			sqlValue = new SQLValue(sql.toString(), dataObjectValue.getValues());
		}
		return executeQuery(sqlValue, new IQueryExtractor<Integer>() {
			@Override
			public Integer extractData(final ResultSet rs) throws SQLException {
				return rs.next() ? rs.getInt(1) : 0;
			}
		});
	}

	/* utils */

	@Override
	public Object exchange(final Object o1, final Object o2, final Column order, final boolean up) {
		if (o1 == null || o2 == null || order == null) {
			return null;
		}
		if (!o1.getClass().equals(o2.getClass())) {
			return null;
		}

		final String orderName;
		final long i1;
		final long i2;

		orderName = order.getColumnSqlName();
		i1 = ((Number) BeanUtils.getProperty(o1, orderName)).longValue();
		i2 = ((Number) BeanUtils.getProperty(o2, orderName)).longValue();
		if (i1 == i2) {
			return null;
		}

		final long max = Math.max(i1, i2);
		final long min = Math.min(i1, i2);
		getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status) {
				final String tn = getTable().getName();
				final StringBuilder sb = new StringBuilder();
				sb.append("update ").append(tn).append(" set ");
				sb.append(orderName).append("=? where ").append(orderName).append("=?");
				final String sql = sb.toString();
				sb.setLength(0);
				sb.append("update ").append(tn).append(" set ").append(orderName);
				sb.append("=").append(orderName).append("+? where ");
				sb.append(orderName).append(">? and ").append(orderName).append("<?");
				final String sql2 = sb.toString();
				if (up) {
					executeUpdate(new SQLValue(sql, new Object[] { -1, min }));
					executeUpdate(new SQLValue(sql, new Object[] { min, max }));
					executeUpdate(new SQLValue(sql2, new Object[] { 1, min, max }));
					executeUpdate(new SQLValue(sql, new Object[] { min + 1, -1 }));
				} else {
					executeUpdate(new SQLValue(sql, new Object[] { -1, max }));
					executeUpdate(new SQLValue(sql, new Object[] { max, min }));
					executeUpdate(new SQLValue(sql2, new Object[] { -1, min, max }));
					executeUpdate(new SQLValue(sql, new Object[] { max - 1, -1 }));
				}
			}
		});
		return new long[] { min, max };
	}

	private static DataFieldMaxValueIncrementer incrementer;

	@Override
	public long nextIncValue(final String column) {
		if (incrementer != null) {
			return incrementer.nextLongValue();
		}
		try {
			final String db = DbUtils.getDatabaseMetaData(dataSource).databaseProductName;
			final String incrementerName = column + "_incrementer";
			// final String incrementerName = table.getName() + "_" + column +
			// "_incrementer";
			if (db.equals(ISQLConstants.Oracle)) {
				if (!DbUtils.isSequenceExists(dataSource, incrementerName)) {
					executeUpdate("create sequence " + incrementerName + " start with 1 increment by 1 nomaxvalue");
				}
				incrementer = new OracleSequenceMaxValueIncrementer(dataSource, incrementerName);
			} else if (db.equals(ISQLConstants.MySQL)) {
				if (!DbUtils.isTableExists(dataSource, incrementerName)) {
					executeUpdate("create table " + incrementerName + " (value bigint not null default 0) engine=MYISAM;");
					executeUpdate("insert into " + incrementerName + " values(0);");
				}
				incrementer = new MySQLMaxValueIncrementer(dataSource, incrementerName, "value");
				((MySQLMaxValueIncrementer) incrementer).setCacheSize(10);
			} else if (db.equals(ISQLConstants.SQLServer)) {
				if (!DbUtils.isTableExists(dataSource, incrementerName)) {
					executeUpdate("create table " + incrementerName + " (value bigint IDENTITY);");
					executeUpdate("insert into " + incrementerName + " default values");
				}
				incrementer = new SqlServerMaxValueIncrementer(dataSource, incrementerName, "value");
			} else if (db.equals(ISQLConstants.DB2)) {
				incrementer = new DB2SequenceMaxValueIncrementer(dataSource, incrementerName);
			}
			return incrementer.nextLongValue();
		} catch (final SQLException e) {
			throw DataObjectException.wrapException(e);
		}
	}

	@Override
	public ID nextId(final String column) {
		return getTable().newID(nextIncValue(column));
	}
}
