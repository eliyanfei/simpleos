package net.simpleframework.ado.db.cache;

import java.util.Map;

import javax.sql.DataSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.simpleframework.core.ado.db.Table;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class EhCacheTableEntityManager extends AbstractCacheTableEntityManager {
	final Ehcache cache;

	public EhCacheTableEntityManager(final DataSource dataSource, final Table table) {
		super(dataSource, table);
		this.cache = new Cache(null, 0, false, false, 0, 0);
	}

	@Override
	protected <T> T getCache(final String key) {
		return null;
	}

	@Override
	protected <T> void putCache(final String key, final T data) {
	}

	@Override
	protected void removeCache(final String key) {
	}

	@Override
	protected Map<String, Object> getMap(final String key) {
		return null;
	}

	@Override
	protected void putMap(final String key, final Map<String, Object> data) {
	}

	@Override
	protected void removeMap(final String key) {
	}
}
