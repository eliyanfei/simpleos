package net.simpleframework.web.page.component.ado;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.IApplicationModuleAware;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentJavascriptCallback;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDbComponentHandle extends IComponentHandle, IApplicationModuleAware,
		IComponentJavascriptCallback {

	ITableEntityManager getTableEntityManager(ComponentParameter compParameter, Class<?> beanClazz);

	ITableEntityManager getTableEntityManager(ComponentParameter compParameter);

	Class<? extends IDataObjectBean> getEntityBeanClass();

	<T extends IDataObjectBean> T getEntityBeanByRequest(ComponentParameter compParameter);

	<T extends IDataObjectBean> T getEntityBeanById(ComponentParameter compParameter, Object id);

	<T extends IDataObjectBean> T getEntityBeanById(ComponentParameter compParameter, Object id,
			Class<T> beanClazz);

	// add
	<T extends IDataObjectBean> T doAdd(ComponentParameter compParameter, Map<String, Object> data);

	<T extends IDataObjectBean> T doAdd(ComponentParameter compParameter, Map<String, Object> data,
			final Class<T> beanClazz);

	// edit
	<T extends IDataObjectBean> T doEdit(ComponentParameter compParameter, Object id,
			Map<String, Object> data);

	<T extends IDataObjectBean> T doEdit(ComponentParameter compParameter, Object id,
			Map<String, Object> data, final Class<T> beanClazz);

	// delete
	void doDelete(ComponentParameter compParameter, IDataObjectValue ev);

	<T extends IDataObjectBean> void doDelete(ComponentParameter compParameter, IDataObjectValue ev,
			Class<T> beanClazz);

	void doUnDelete(ComponentParameter compParameter, IDataObjectValue ev);

	<T extends IDataObjectBean> void doUnDelete(ComponentParameter compParameter,
			IDataObjectValue ev, Class<T> beanClazz);

	void doExchange(ComponentParameter compParameter, Object id, Object id2, boolean up);

	<T extends IDataObjectBean> void doExchange(ComponentParameter compParameter, Object id,
			Object id2, boolean up, Class<T> beanClazz);

	/*--------------------------------- utils -----------------------------------*/

	String getIdParameterName(ComponentParameter compParameter);

	public static class Utils {
		private static final String MANAGER_KEY = "IEntityBeanHandle.isManager";

		public static boolean isManager(final ComponentParameter compParameter) {
			return isManager(compParameter,
					((IDbComponentHandle) compParameter.getComponentHandle()).getManager(compParameter));
		}

		public static boolean isManager(final PageRequestResponse requestResponse,
				final String manager) {
			Boolean member = (Boolean) requestResponse.getRequestAttribute(MANAGER_KEY);
			if (member == null) {
				try {
					requestResponse.setRequestAttribute(
							MANAGER_KEY,
							member = (Boolean) ReflectUtils.methodIsMember.invoke(null, manager,
									requestResponse.getSession(), null));
				} catch (final Exception e) {
					ALoggerAware.getLogger(Utils.class).warn(e);
				}
			}
			return member;
		}
	}

	class DbComponentWrapper {
		private final IDbComponentWrapperCallback callback;

		private final Map<Class<?>, Table> tables = new HashMap<Class<?>, Table>();

		public DbComponentWrapper(final IDbComponentWrapperCallback callback) {
			this.callback = callback;
			callback.putTables(tables);
		}

		public ITableEntityManager getTableEntityManager(final ComponentParameter compParameter,
				final Class<?> beanClazz) {
			final IApplicationModule applicationModule = ((IDbComponentHandle) callback)
					.getApplicationModule();
			if (applicationModule != null) {
				final ITableEntityManager temgr = DataObjectManagerUtils.getTableEntityManager(
						applicationModule, beanClazz);
				if (temgr != null) {
					return temgr;
				}
			}
			return DataObjectManagerUtils.getTableEntityManager(
					PageUtils.pageContext.getApplication(), tables, beanClazz);
		}

		public <T extends IDataObjectBean> T getEntityBeanById(
				final ComponentParameter compParameter, final Object id, final Class<T> beanClazz) {
			return getTableEntityManager(compParameter, beanClazz).queryForObjectById(id, beanClazz);
		}

		public <T extends IDataObjectBean> T doEdit(final ComponentParameter compParameter,
				final Object id, final Map<String, Object> data, final Class<T> beanClazz) {
			final T t = getEntityBeanById(compParameter, id, beanClazz);
			if (t != null) {
				final ITableEntityManager temgr = getTableEntityManager(compParameter, beanClazz);
				temgr.updateTransaction(t, new TableEntityAdapter() {
					@Override
					public void beforeUpdate(final ITableEntityManager manager, final Object[] objects) {
						try {
							callback.doBeforeEdit(compParameter, temgr, t, data, beanClazz);
						} catch (final Exception e) {
							throw DataObjectException.wrapException(e);
						}
					}

					@Override
					public void afterUpdate(final ITableEntityManager manager, final Object[] objects) {
						try {
							callback.doEditCallback(compParameter, temgr, t, data, beanClazz);
						} catch (final Exception e) {
							throw DataObjectException.wrapException(e);
						}
					}
				});
			}
			return t;
		}

		public <T extends IDataObjectBean> T doAdd(final ComponentParameter compParameter,
				final Map<String, Object> data, final Class<T> beanClazz) {
			final T t = BeanUtils.newInstance(beanClazz);
			final ITableEntityManager temgr = getTableEntityManager(compParameter, beanClazz);
			assertPostTime(compParameter);
			temgr.insertTransaction(t, new TableEntityAdapter() {
				@Override
				public void beforeInsert(final ITableEntityManager manager, final Object[] objects) {
					try {
						callback.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
					} catch (final Exception e) {
						throw DataObjectException.wrapException(e);
					}
				}

				@Override
				public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
					try {
						callback.doAddCallback(compParameter, temgr, t, data, beanClazz);
					} catch (final Exception e) {
						throw DataObjectException.wrapException(e);
					}
				}
			});
			setPostTime(compParameter);
			return t;
		}

		public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter,
				final IDataObjectValue ev, final Class<T> beanClazz) {
			getTableEntityManager(compParameter, beanClazz).deleteTransaction(ev,
					new TableEntityAdapter() {
						@Override
						public void beforeDelete(final ITableEntityManager manager,
								final IDataObjectValue dataObjectValue) {
							try {
								callback.doBeforeDelete(compParameter, ev, beanClazz);
							} catch (final Exception e) {
								throw DataObjectException.wrapException(e);
							}
						}

						@Override
						public void afterDelete(final ITableEntityManager manager,
								final IDataObjectValue dataObjectValue) {
							try {
								callback.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
							} catch (final Exception e) {
								throw DataObjectException.wrapException(e);
							}
						}
					});
		}

		public <T extends IDataObjectBean> void doExchange(final ComponentParameter compParameter,
				final Object id, final Object id2, final boolean up, final Class<T> beanClazz) {
			final IDataObjectBean idBean = getEntityBeanById(compParameter, id, beanClazz);
			final IDataObjectBean idBean2 = getEntityBeanById(compParameter, id2, beanClazz);
			getTableEntityManager(compParameter, beanClazz).exchange(idBean, idBean2,
					new Column("oorder"), up);
		}

		private static String LAST_ADD_TIME = "lasttime_";

		private void assertPostTime(final PageRequestResponse requestResponse) {
			final long interval = callback.getPostInterval();
			if (interval == 0) {
				return;
			}
			final Long l = (Long) requestResponse.getSessionAttribute(LAST_ADD_TIME
					+ getClass().getSimpleName());
			if (l != null) {
				if (System.currentTimeMillis() - l.longValue() < interval) {
					throw DataObjectException.wrapException(LocaleI18n.getMessage(
							"AbstractContentPagerHandle.0", interval / 1000));
				}
			}
		}

		private void setPostTime(final PageRequestResponse requestResponse) {
			final long interval = callback.getPostInterval();
			if (interval == 0) {
				return;
			}
			requestResponse.setSessionAttribute(LAST_ADD_TIME + getClass().getSimpleName(),
					System.currentTimeMillis());
		}
	}

	interface IDbComponentWrapperCallback {
		void putTables(final Map<Class<?>, Table> tables);

		<T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter,
				final ITableEntityManager temgr, final T t, final Map<String, Object> data,
				final Class<T> beanClazz);

		<T extends IDataObjectBean> void doEditCallback(final ComponentParameter compParameter,
				final ITableEntityManager temgr, final T t, final Map<String, Object> data,
				final Class<T> beanClazz);

		<T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter,
				final ITableEntityManager temgr, final T t, final Map<String, Object> data,
				final Class<T> beanClazz);

		<T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter,
				final ITableEntityManager temgr, final T t, final Map<String, Object> data,
				final Class<T> beanClazz);

		<T extends IDataObjectBean> void doBeforeDelete(final ComponentParameter compParameter,
				final IDataObjectValue dataObjectValue, final Class<T> beanClazz);

		<T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter,
				final IDataObjectValue dataObjectValue, final Class<T> beanClazz);

		long getPostInterval();
	}
}