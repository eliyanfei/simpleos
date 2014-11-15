package net.simpleframework.ado.db.cache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import net.simpleframework.core.ado.db.Table;

import org.apache.commons.collections.map.LRUMap;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MapTableEntityManager extends AbstractCacheTableEntityManager {

	private int maxCacheSize = 0;

	private Map<String, Object> cache;

	private Map<String, Map<String, Object>> map;

	public MapTableEntityManager(final DataSource dataSource, final Table table) {
		super(dataSource, table);
		setMaxCacheSize(0);
	}

	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	@SuppressWarnings("unchecked")
	public void setMaxCacheSize(final int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
		if (maxCacheSize > 0) {
			cache = Collections.synchronizedMap(new LRUMap(maxCacheSize));
			map = Collections.synchronizedMap(new LRUMap(maxCacheSize));
		} else {
			cache = new ConcurrentHashMap<String, Object>();
			map = new ConcurrentHashMap<String, Map<String, Object>>();
		}
	}

	@Override
	public synchronized void reset() {
		cache.clear();
		map.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getCache(final String key) {
		return (T) cache.get(key);
	}

	@Override
	protected <T> void putCache(final String key, final T data) {
		cache.put(key, data);
	}

	@Override
	protected void removeCache(final String key) {
		cache.remove(key);
	}

	@Override
	protected Map<String, Object> getMap(final String key) {
		return map.get(key);
	}

	@Override
	protected void putMap(final String key, final Map<String, Object> data) {
		map.put(key, data);
	}

	@Override
	protected void removeMap(final String key) {
		map.remove(key);
	}
}
