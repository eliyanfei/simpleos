package net.simpleframework.ado;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.simpleframework.ado.db.IEntityManager;
import net.simpleframework.ado.db.IQueryEntityManager;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.QueryEntityManager;
import net.simpleframework.ado.db.TableEntityManager;
import net.simpleframework.ado.db.cache.MapTableEntityManager;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DataObjectManagerFactory {

	public static ITableEntityManager getTableEntityManager(final DataSource dataSource,
			final Table table) {
		return getTableEntityManager(dataSource, table, defaultTableEntityManagerCreator);
	}

	private static ITableEntityManagerCreator defaultTableEntityManagerCreator = new ITableEntityManagerCreator() {
		@Override
		public ITableEntityManager createTableEntityManager(final DataSource dataSource,
				final Table table) {
			return new TableEntityManager(dataSource, table);
		}
	};

	public static ITableEntityManager getTableEntityManager(final DataSource dataSource,
			final Table table, final ITableEntityManagerCreator creator) {
		final String urlHash = "table_"
				+ StringUtils.hash(DbUtils.getDatabaseMetaData(dataSource).url);
		final String key = urlHash + "_" + table;
		ITableEntityManager instance = (ITableEntityManager) managers.get(key);
		if (instance == null) {
			managers.put(key, instance = creator.createTableEntityManager(dataSource, table));
		}
		return instance;
	}

	public static interface ITableEntityManagerCreator {

		ITableEntityManager createTableEntityManager(DataSource dataSource, Table table);
	}

	public static IQueryEntityManager getQueryEntityManager(final DataSource dataSource) {
		final String urlHash = "query_"
				+ StringUtils.hash(DbUtils.getDatabaseMetaData(dataSource).url);
		IQueryEntityManager instance = (IQueryEntityManager) managers.get(urlHash);
		if (instance == null) {
			managers.put(urlHash, instance = new QueryEntityManager(dataSource));
		}
		return instance;
	}

	public static void resetAll() {
		for (final IEntityManager mgr : managers.values()) {
			if (mgr instanceof MapTableEntityManager) {
				((MapTableEntityManager) mgr).reset();
			}
		}
	}

	private static Map<String, IEntityManager> managers = new ConcurrentHashMap<String, IEntityManager>();
}
