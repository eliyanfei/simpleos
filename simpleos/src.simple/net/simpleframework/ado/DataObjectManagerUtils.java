package net.simpleframework.ado;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.ado.db.IQueryEntityManager;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.TableEntityManager;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.Logger;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DataObjectManagerUtils {
	static Logger logger = ALoggerAware.getLogger(DataObjectManagerUtils.class);

	private static DataObjectManagerFactory.ITableEntityManagerCreator tableEntityManagerCreator;

	public static IQueryEntityManager getQueryEntityManager(final IApplication application) {
		return DataObjectManagerFactory.getQueryEntityManager(application.getDataSource());
	}

	public static ITableEntityManager getTableEntityManager(final IApplication application,
			final Table table) {
		if (tableEntityManagerCreator == null) {
			tableEntityManagerCreator = new DataObjectManagerFactory.ITableEntityManagerCreator() {
				@Override
				public ITableEntityManager createTableEntityManager(final DataSource dataSource,
						final Table table) {
					final String managerClass = StringUtils.trimOneLine(application
							.getApplicationConfig().getTableEntityManagerClass());
					try {
						return (ITableEntityManager) BeanUtils.forName(managerClass)
								.getConstructor(DataSource.class, Table.class)
								.newInstance(dataSource, table);
					} catch (final Exception e) {
						logger.warn(e);
					}
					return new TableEntityManager(dataSource, table);
				}
			};
		}
		return DataObjectManagerFactory.getTableEntityManager(application.getDataSource(), table,
				tableEntityManagerCreator);
	}

	public static ITableEntityManager getTableEntityManager(final IApplication application,
			final Map<Class<?>, Table> tables, final Class<?> beanClazz) {
		if (tables.size() == 0 || beanClazz == null) {
			return null;
		}
		Table table = null;
		for (final Map.Entry<Class<?>, Table> entry : tables.entrySet()) {
			final Class<?> beanClazz2 = entry.getKey();
			if (beanClazz2.isAssignableFrom(beanClazz) || beanClazz.isAssignableFrom(beanClazz2)) {
				table = entry.getValue();
				break;
			}
		}
		return table == null ? null : getTableEntityManager(application, table);
	}

	private static final Map<IApplicationModule, Map<Class<?>, Table>> moduleTables = new HashMap<IApplicationModule, Map<Class<?>, Table>>();

	public static Map<Class<?>, Table> getApplicationModuleTables(
			final IApplicationModule applicationModule) {
		Map<Class<?>, Table> tables = moduleTables.get(applicationModule);
		if (tables == null) {
			moduleTables.put(applicationModule, tables = new HashMap<Class<?>, Table>());
		}
		return tables;
	}

	public static ITableEntityManager getTableEntityManager(
			final IApplicationModule applicationModule, final Class<?> beanClazz) {
		return getTableEntityManager(applicationModule.getApplication(),
				getApplicationModuleTables(applicationModule), beanClazz);
	}

	public static ITableEntityManager getTableEntityManager(
			final IApplicationModule applicationModule) {
		return getTableEntityManager(applicationModule, applicationModule.getEntityBeanClass());
	}
}
