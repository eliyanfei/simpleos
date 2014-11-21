package net.simpleframework.web.page.component.ado;

import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ado.IDbComponentHandle.IDbComponentWrapperCallback;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDbComponentHandle extends AbstractComponentHandle implements
		IDbComponentHandle, IDbComponentWrapperCallback {
	@Override
	public IApplicationModule getApplicationModule() {
		return null;
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter,
			final String jsAction, final Object bean) {
		return null;
	}

	@Override
	protected String getDefaultjob(final ComponentParameter compParameter) {
		return getManager(compParameter);
	}

	/*---------------------- IDbComponentHandle ----------------*/

	private final DbComponentWrapper wrapper = new DbComponentWrapper(this);

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter,
			final Class<?> beanClazz) {
		return wrapper.getTableEntityManager(compParameter, beanClazz);
	}

	@Override
	public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter) {
		return getTableEntityManager(compParameter, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T getEntityBeanByRequest(
			final ComponentParameter compParameter) {
		return getEntityBeanById(compParameter,
				compParameter.getRequestParameter(getIdParameterName(compParameter)));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IDataObjectBean> T getEntityBeanById(final ComponentParameter compParameter,
			final Object id) {
		return (T) getEntityBeanById(compParameter, id, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T getEntityBeanById(final ComponentParameter compParameter,
			final Object id, final Class<T> beanClazz) {
		return wrapper.getEntityBeanById(compParameter, id, beanClazz);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IDataObjectBean> T doEdit(final ComponentParameter compParameter,
			final Object id, final Map<String, Object> data) {
		return (T) doEdit(compParameter, id, data, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T doEdit(final ComponentParameter compParameter,
			final Object id, final Map<String, Object> data, final Class<T> beanClazz) {
		return wrapper.doEdit(compParameter, id, data, beanClazz);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IDataObjectBean> T doAdd(final ComponentParameter compParameter,
			final Map<String, Object> data) {
		return (T) doAdd(compParameter, data, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> T doAdd(final ComponentParameter compParameter,
			final Map<String, Object> data, final Class<T> beanClazz) {
		return wrapper.doAdd(compParameter, data, beanClazz);
	}

	@Override
	public void doDelete(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue) {
		doDelete(compParameter, dataObjectValue, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue, final Class<T> beanClazz) {
		wrapper.doDelete(compParameter, dataObjectValue, beanClazz);
	}

	@Override
	public void doUnDelete(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue) {
		doUnDelete(compParameter, dataObjectValue, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> void doUnDelete(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue, final Class<T> beanClazz) {
	}

	@Override
	public void doExchange(final ComponentParameter compParameter, final Object id,
			final Object id2, final boolean up) {
		doExchange(compParameter, id, id2, up, getEntityBeanClass());
	}

	@Override
	public <T extends IDataObjectBean> void doExchange(final ComponentParameter compParameter,
			final Object id, final Object id2, final boolean up, final Class<T> beanClazz) {
		wrapper.doExchange(compParameter, id, id2, up, beanClazz);
	}

	/*---------------------- IDbComponentWrapperCallback ----------------*/

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter,
			final ITableEntityManager temgr, final T t, final Map<String, Object> data,
			final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeDelete(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue, final Class<T> beanClazz) {
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter,
			final IDataObjectValue dataObjectValue, final Class<T> beanClazz) {
	}

	@Override
	public long getPostInterval() {
		return 0;
	}
}
