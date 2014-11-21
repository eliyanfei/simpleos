package net.simpleframework.ado.db;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.core.AAttributeAware;
import net.simpleframework.core.ado.IDataObjectListener;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractBeanManager<T extends IDataObjectBean> extends AAttributeAware
		implements IBeanManagerAware<T> {
	private ITableEntityManager tableEntityManager;

	@Override
	public ITableEntityManager getEntityManager() {
		if (tableEntityManager == null) {
			tableEntityManager = DataObjectManagerUtils.getTableEntityManager(getApplicationModule(),
					getBeanClass());
		}
		return tableEntityManager;
	}

	@Override
	public void addListener(final IDataObjectListener listener) {
		getEntityManager().addListener(listener);
	}

	@Override
	public IQueryEntitySet<T> query() {
		return query(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IQueryEntitySet<T> query(final IDataObjectValue dataObjectValue) {
		return (IQueryEntitySet<T>) getEntityManager().query(dataObjectValue, getBeanClass());
	}

	public T queryForObject(final IDataObjectValue dataObjectValue) {
		return getEntityManager().queryForObject(dataObjectValue, getBeanClass());
	}

	@Override
	public T queryForObjectById(final Object id) {
		return getEntityManager().queryForObjectById(id, getBeanClass());
	}

	@Override
	public int update(final T bean) {
		return getEntityManager().update(bean);
	}

	@Override
	public int update(final String[] columns, final T bean) {
		return getEntityManager().update(columns, bean);
	}

	@Override
	public int insert(final T bean) {
		return getEntityManager().insert(bean);
	}

	@Override
	public int delete(final IDataObjectValue dataObjectValue) {
		return getEntityManager().delete(dataObjectValue);
	}

	public Table getTable() {
		return getEntityManager().getTable();
	}

	@Override
	public ID id(final Object id) {
		return getTable().newID(id);
	}

	@Override
	public String tblname() {
		return getTable().getName();
	}

	@SuppressWarnings("unchecked")
	public Class<? extends T> getBeanClass() {
		final Type[] types = ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments();
		if (types != null && types.length > 0) {
			return (Class<T>) types[0];
		} else {
			return null;
		}
	}
}
