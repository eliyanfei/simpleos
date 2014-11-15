package net.simpleframework.ado.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.db.event.IEntityListener;
import net.simpleframework.core.ado.AbstractDataObjectManager;
import net.simpleframework.core.ado.IDataObjectListener;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LinkedCaseInsensitiveMap;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractEntityManager extends AbstractDataObjectManager implements IEntityManager {

	protected final DataSource dataSource;

	protected final JdbcTemplate jt;

	private TransactionTemplate transactionTemplate;

	public AbstractEntityManager(final DataSource dataSource) {
		this.dataSource = dataSource;
		jt = new JdbcTemplate(dataSource);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	public TransactionTemplate getTransactionTemplate() {
		if (transactionTemplate == null) {
			transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
		}
		return transactionTemplate;
	}

	@Override
	public <T> T executeQuery(final SQLValue value, final IQueryExtractor<T> extractor, final int resultSetType) {
		final Object[] args = value.getValues();
		final PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(value.getSql(), getSqlTypes(args));
		factory.setResultSetType(resultSetType);
		return jt.query(factory.newPreparedStatementCreator(ResultSetUtils.getParameterValues(jt.getDataSource(), args)), extractor);
	}

	@Override
	public <T> T executeQuery(final SQLValue value, final IQueryExtractor<T> extractor) {
		return executeQuery(value, extractor, ResultSet.TYPE_FORWARD_ONLY);
	}

	protected int executeUpdate(final String sql) {
		return executeUpdate(new SQLValue(sql));
	}

	protected int executeUpdate(final SQLValue value) {
		final Object[] args = value.getValues();
		final PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(value.getSql(), getSqlTypes(args));
		return jt.update(factory.newPreparedStatementCreator(ResultSetUtils.getParameterValues(jt.getDataSource(), args)));
	}

	@Override
	public <T> T execute(final IConnectionAware<T> connection) {
		return jt.execute(connection);
	}

	@Override
	public int execute(final SQLValue sqlValue, final IEntityListener l) {
		return execute(new SQLValue[] { sqlValue }, l);
	}

	@Override
	public int execute(final SQLValue sqlValue) {
		return execute(sqlValue, null);
	}

	@Override
	public int execute(final SQLValue[] sqlValues, final IEntityListener l) {
		if (sqlValues == null || sqlValues.length == 0) {
			return 0;
		}
		if (l != null) {
			l.beforeExecute(this, sqlValues);
		}
		final Collection<IDataObjectListener> listeners = getListeners();
		for (final IDataObjectListener listener : listeners) {
			((IEntityListener) listener).beforeExecute(this, sqlValues);
		}
		int ret = 0;
		for (final SQLValue sqlValue : sqlValues) {
			ret += executeUpdate(sqlValue);
		}
		if (l != null) {
			l.afterExecute(this, sqlValues);
		}
		for (final IDataObjectListener listener : listeners) {
			((IEntityListener) listener).afterExecute(this, sqlValues);
		}
		reset();
		return ret;
	}

	@Override
	public int execute(final SQLValue[] sqlValues) {
		return execute(sqlValues, null);
	}

	@Override
	public int executeTransaction(final SQLValue sqlValue, final IEntityListener l) {
		return executeTransaction(new SQLValue[] { sqlValue }, l);
	}

	@Override
	public int executeTransaction(final SQLValue sqlValue) {
		return executeTransaction(sqlValue, null);
	}

	@Override
	public int executeTransaction(final SQLValue[] sqlValues, final IEntityListener l) {
		return (Integer) getTransactionTemplate().execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(final TransactionStatus status) {
				return execute(sqlValues, l);
			}
		});
	}

	@Override
	public int executeTransaction(final SQLValue[] sqlValues) {
		return executeTransaction(sqlValues, null);
	}

	@Override
	public int[] batchUpdate(final String[] sql) {
		return jt.batchUpdate(sql);
	}

	@Override
	public int[] batchUpdate(final String sql, final int batchCount, final IBatchValueSetter setter) {
		return jt.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(final PreparedStatement ps, final int row) throws SQLException {
				setter.setValues(ps, row);
			}

			@Override
			public int getBatchSize() {
				return batchCount;
			}
		});
	}

	protected Column[] getColumns(final String[] columns) {
		if (columns != null) {
			final Column[] objects = new Column[columns.length];
			for (int i = 0; i < columns.length; i++) {
				objects[i] = new Column(columns[i]);
			}
			return objects;
		} else {
			return null;
		}
	}

	protected Map<String, Object> mapRowData(final Object[] columns, final ResultSet rs) throws SQLException {
		Map<String, Object> mapData;
		if (columns != null) {
			final int columnCount = columns.length;
			mapData = new LinkedCaseInsensitiveMap<Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				final Object column = columns[i - 1];
				final String key = column.toString();
				final Object obj = ResultSetUtils.getResultSetValue(jt.getDataSource(), rs, i,
						((column instanceof Column) ? ((Column) column).getBeanPropertyType() : null));
				mapData.put(key, obj);
			}
		} else {
			final ResultSetMetaData rsmd = rs.getMetaData();
			final int columnCount = rsmd.getColumnCount();
			mapData = new LinkedCaseInsensitiveMap<Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				final String key = ResultSetUtils.lookupColumnName(rsmd, i);
				final Object obj = ResultSetUtils.getResultSetValue(jt.getDataSource(), rs, i, null);
				mapData.put(key, obj);
			}
		}
		return mapData;
	}

	protected <T> BeanWrapper<T> getBeanWrapper(final Object[] columns, final Class<T> beanClass) {
		return new BeanWrapper<T>(columns, beanClass);
	}

	protected class BeanWrapper<T> {
		private final Collection<PropertyCache> collection;

		private final Class<T> beanClass;

		private BeanWrapper(final Object[] columns, final Class<T> beanClass) {
			this.beanClass = beanClass;

			final PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(beanClass);
			collection = new ArrayList<PropertyCache>(descriptors.length);

			T bean = null;
			try {
				bean = beanClass.newInstance();
			} catch (final Exception e) {
				logger.error(e);
				return;
			}

			for (final PropertyDescriptor descriptor : descriptors) {
				final Method readMethod = BeanUtils.getReadMethod(descriptor);
				if (readMethod == null) {
					continue;
				}
				final Method writeMethod = BeanUtils.getWriteMethod(descriptor);
				if (writeMethod == null) {
					continue;
				}

				final String propertyName = descriptor.getName();
				String columnName = propertyName;
				if (bean instanceof IEntityBeanAware) {
					final Map<String, Column> tableColumns = ((IEntityBeanAware) bean).getTableColumnDefinition();
					if (tableColumns != null) {
						final Column tableColumn = tableColumns.get(propertyName);
						if (tableColumn != null) {
							columnName = tableColumn.getColumnName();
						} else {
							continue;
						}
					}
				}

				if (columns != null && columns.length > 0) {
					boolean find = false;
					for (final Object column : columns) {
						if (columnName.equalsIgnoreCase(column.toString())) {
							find = true;
							break;
						}
					}
					if (!find) {
						continue;
					}
				}

				final PropertyCache cache = new PropertyCache();
				cache.propertyType = descriptor.getPropertyType();
				cache.writeMethod = writeMethod;
				cache.columnName = columnName;
				collection.add(cache);
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public T toBean(final ResultSet rs) throws SQLException {
			T bean = null;
			try {
				bean = beanClass.newInstance();
			} catch (final Exception e) {
				logger.error(e);
				return null;
			}

			for (final PropertyCache cache : collection) {
				if (cache.sqlColumnIndex <= 0) {
					final int sqlColumnIndex = ResultSetUtils.lookupColumnIndex(rs.getMetaData(), cache.columnName);
					if (sqlColumnIndex <= 0) {
						continue;
					} else {
						cache.sqlColumnIndex = sqlColumnIndex;
					}
				}

				final Class<?> propertyType = cache.propertyType;
				Object object = ResultSetUtils.getResultSetValue(jt.getDataSource(), rs, cache.sqlColumnIndex, propertyType);
				if (object == null) {
					continue;
				}

				try {
					if (Enum.class.isAssignableFrom(propertyType)) {
						object = ConvertUtils.toEnum((Class<? extends Enum>) propertyType, object);
					} else if (ID.class.isAssignableFrom(propertyType)) {
						if (AbstractEntityManager.this instanceof TableEntityManager) {
							object = ((TableEntityManager) AbstractEntityManager.this).getTable().newID(object);
						} else {
							object = ID.Utils.newID(object);
						}
					} else if (Date.class.isAssignableFrom(propertyType)) {
						object = new Date(((Date) object).getTime());
					}
					cache.writeMethod.invoke(bean, object);
				} catch (final Throwable e) {
					logger.error(e);
				}
			}
			return bean;
		}
	}

	private class PropertyCache {
		String columnName;

		int sqlColumnIndex;

		Method writeMethod;

		Class<?> propertyType;
	}

	private int[] getSqlTypes(final Object[] args) {
		if (args == null) {
			return null;
		}
		final int[] sqlTypes = new int[args.length];
		for (int i = 0; i < args.length; i++) {
			sqlTypes[i] = SqlTypeValue.TYPE_UNKNOWN;
		}
		return sqlTypes;
	}

	protected <T> T createQueryObject(final Object[] columns, final SQLValue sqlValue, final Class<T> beanClass) {
		final BeanWrapper<T> wrapper = getBeanWrapper(columns, beanClass);
		return executeQuery(sqlValue, new IQueryExtractor<T>() {
			@Override
			public T extractData(final ResultSet rs) throws SQLException {
				return rs.next() ? wrapper.toBean(rs) : null;
			}
		});
	}

	protected Map<String, Object> createQueryMap(final Object[] columns, final SQLValue sqlValue) {
		return executeQuery(sqlValue, new IQueryExtractor<Map<String, Object>>() {
			@Override
			public Map<String, Object> extractData(final ResultSet rs) throws SQLException {
				return rs.next() ? mapRowData(columns, rs) : null;
			}
		});
	}

	protected AbstractQueryEntitySet<Map<String, Object>> createQueryEntitySet(final Object[] columns, final SQLValue sqlValue) {
		return new AbstractQueryEntitySet<Map<String, Object>>(this, sqlValue) {
			@Override
			public Map<String, Object> mapRow(final ResultSet rs, final int rowNum) throws SQLException {
				return mapRowData(columns, rs);
			}
		};
	}

	protected <T> AbstractQueryEntitySet<T> createQueryEntitySet(final Object[] columns, final SQLValue sqlValue, final Class<T> beanClass) {
		final BeanWrapper<T> wrapper = getBeanWrapper(columns, beanClass);
		return new AbstractQueryEntitySet<T>(this, sqlValue) {
			@Override
			public T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
				return wrapper.toBean(rs);
			}
		};
	}
}
