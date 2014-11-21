package net.simpleframework.ado.db.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.AbstractDataObjectValue;
import net.simpleframework.ado.db.AbstractQueryEntitySet;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IEntityBeanAware;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.ado.db.TableEntityManager;
import net.simpleframework.ado.db.event.IEntityListener;
import net.simpleframework.ado.db.event.ITableEntityListener;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.BeanUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractCacheTableEntityManager extends TableEntityManager {

	public AbstractCacheTableEntityManager(final DataSource dataSource, final Table table) {
		super(dataSource, table);
	}

	protected String toUniqueString(final Object object) {
		if (object == null) {
			return null;
		}
		final Table table = getTable();
		if (table.isNoCache()) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		if (object instanceof SQLValue) {
			sb.append(((SQLValue) object).key());
		} else if (object instanceof IDataObjectValue) {
			sb.append(table.getName());
			sb.append("-").append(((IDataObjectValue) object).key());
		} else {
			sb.append(table.getName());
			for (final String uniqueColumn : table.getUniqueColumns()) {
				sb.append("-");
				try {
					if (object instanceof ResultSet) {
						sb.append(AbstractDataObjectValue.valueToString(((ResultSet) object).getObject(uniqueColumn)));
					} else {
						String propertyName = uniqueColumn;
						if (object instanceof IEntityBeanAware) {
							propertyName = IEntityBeanAware.Utils.getPropertyName(object, uniqueColumn);
						}
						sb.append(AbstractDataObjectValue.valueToString(BeanUtils.getProperty(object, propertyName)));
					}
				} catch (final Exception e) {
					return null;
				}
			}
		}
		return sb.toString();
	}

	@Override
	protected int delete(final IDataObjectValue dataObjectValue, final ITableEntityListener l) {
		try {
			return super.delete(dataObjectValue, l);
		} finally {
			reset();
		}
	}

	@Override
	protected int update(final Object[] columns, final Object[] objects, final ITableEntityListener l) {
		doUpdateObjects(objects);
		return super.update(columns, objects, l);
	}

	protected void doUpdateObjects(final Object... objects) {
		if (objects == null) {
			return;
		}
		final Map<String, AbstractCacheTableEntityManager> updates = transThreadLocal.get();
		if (updates != null) {
			for (final Object object : objects) {
				final String key = toUniqueString(object);
				if (key != null) {
					updates.put(key, this);
				}
			}
		}
	}

	private static ThreadLocal<Map<String, AbstractCacheTableEntityManager>> transThreadLocal = new ThreadLocal<Map<String, AbstractCacheTableEntityManager>>();

	private void doTransactionBegin() {
		if (transThreadLocal.get() == null) {
			transThreadLocal.set(new HashMap<String, AbstractCacheTableEntityManager>());
		}
	}

	private void doTransactionEnd() {
		final Map<String, AbstractCacheTableEntityManager> updates = transThreadLocal.get();
		if (updates != null) {
			for (final Map.Entry<String, AbstractCacheTableEntityManager> entry : updates.entrySet()) {
				final String key = entry.getKey();
				final AbstractCacheTableEntityManager mgr = entry.getValue();
				mgr.removeCache(key);
				mgr.removeMap(key);
			}
			transThreadLocal.remove();
		}
	}

	@Override
	public int deleteTransaction(final IDataObjectValue dataObjectValue, final ITableEntityListener l) {
		try {
			doTransactionBegin();
			return super.deleteTransaction(dataObjectValue, l);
		} finally {
			doTransactionEnd();
		}
	}

	@Override
	public int insertTransaction(final Object[] objects, final ITableEntityListener l) {
		try {
			doTransactionBegin();
			return super.insertTransaction(objects, l);
		} finally {
			doTransactionEnd();
		}
	}

	@Override
	public int updateTransaction(final Object[] columns, final Object[] objects, final ITableEntityListener l) {
		try {
			doTransactionBegin();
			return super.updateTransaction(columns, objects, l);
		} finally {
			doTransactionEnd();
		}
	}

	protected abstract void removeCache(String key);

	protected abstract <T> T getCache(String key);

	protected abstract <T> void putCache(String key, T data);

	@Override
	public <T> T queryForObject(final Object[] columns, final IDataObjectValue dataObjectValue, final Class<T> beanClass) {
		final String key = toUniqueString(dataObjectValue);
		if (key == null) {
			return super.queryForObject(columns, dataObjectValue, beanClass);
		}
		T t = getCache(key);
		if (t == null) {
			// 此处传递null，而非columns，目的是在缓存情况下，忽略columns参数
			// 以后再考虑更好的方法
			if ((t = super.queryForObject(null/* columns */, dataObjectValue, beanClass)) != null) {
				putCache(key, t);
			}
		} else {
			doUpdateObjects(t);
		}
		return t;
	}

	@Override
	public <T> IQueryEntitySet<T> query(final Object[] columns, final IDataObjectValue dataObjectValue, final Class<T> beanClass) {
		if (getTable().isNoCache()) {
			return super.query(columns, dataObjectValue, beanClass);
		} else {
			final BeanWrapper<T> wrapper = getBeanWrapper(columns, beanClass);
			return new AbstractQueryEntitySet<T>(this, createSQLValue(columns, dataObjectValue)) {
				@Override
				public T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
					final String key = toUniqueString(rs);
					if (key == null) {
						return wrapper.toBean(rs);
					}
					T t = getCache(key);
					if (t == null) {
						if ((t = wrapper.toBean(rs)) != null) {
							putCache(key, t);
						}
					}
					return t;
				}
			};
		}
	}

	/************************* Map Object ************************/

	protected abstract Map<String, Object> getMap(String key);

	protected abstract void putMap(String key, Map<String, Object> data);

	protected abstract void removeMap(String key);

	@Override
	public Map<String, Object> queryForMap(final Object[] columns, final IDataObjectValue dataObjectValue) {
		final String key = toUniqueString(dataObjectValue);
		if (key == null) {
			return super.queryForMap(columns, dataObjectValue);
		}
		Map<String, Object> data = getMap(key);
		if (data == null) {
			if ((data = super.queryForMap(columns, dataObjectValue)) != null) {
				putMap(key, data);
			}
		} else {
			if (columns != null && columns.length > 0 && !data.containsKey(columns[0])) {
				final Map<String, Object> nData = super.queryForMap(columns, dataObjectValue);
				if (nData != null) {
					data.putAll(nData);
				}
			}
			doUpdateObjects(data);
		}
		return data;
	}

	@Override
	public IQueryEntitySet<Map<String, Object>> query(final Object[] columns, final IDataObjectValue dataObjectValue) {
		if (getTable().isNoCache()) {
			return super.query(columns, dataObjectValue);
		} else {
			return new AbstractQueryEntitySet<Map<String, Object>>(this, createSQLValue(columns, dataObjectValue)) {
				@Override
				public Map<String, Object> mapRow(final ResultSet rs, final int rowNum) throws SQLException {
					final String key = toUniqueString(rs);
					if (key == null) {
						return mapRowData(columns, rs);
					}
					Map<String, Object> data = getMap(key);
					if (data == null) {
						if ((data = mapRowData(columns, rs)) != null) {
							putMap(key, data);
						}
					} else {
						if (columns != null && columns.length > 0 && !data.containsKey(columns[0])) {
							final Map<String, Object> nData = mapRowData(columns, rs);
							if (nData != null) {
								data.putAll(nData);
							}
						}
					}
					return data;
				}
			};
		}
	}

	/************************* ope ************************/
	@Override
	public int execute(final SQLValue[] sqlValues, final IEntityListener l) {
		final int ret = super.execute(sqlValues, l);
		boolean delete = false;
		for (final SQLValue sqlValue : sqlValues) {
			if (sqlValue.getSql().trim().toLowerCase().startsWith("delete")) {
				delete = true;
				break;
			}
		}
		if (delete) {
			reset();
		}
		return ret;
	}

	@Override
	public Object exchange(final Object o1, final Object o2, final Column order, final boolean up) {
		final long[] ret = (long[]) super.exchange(o1, o2, order, up);
		final String orderName = order.getColumnSqlName();
		final StringBuilder sb = new StringBuilder();
		sb.append(orderName).append(">=? and ").append(orderName).append("<=?");
		try {
			final IQueryEntitySet<Map<String, Object>> qs = query(new ExpressionValue(sb.toString(), new Object[] { ret[0], ret[1] }));
			Map<String, Object> mObj;
			while ((mObj = qs.next()) != null) {
				final String key = toUniqueString(mObj);
				if (key != null) {
					removeCache(key);
					removeMap(key);
				}
			}
			return ret;
		} catch (final Throwable th) {
			reset();
			throw DataObjectException.wrapException(th);
		}
	}
}
