package net.itsite.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.simpleframework.util.ConvertUtils;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.CompositeCacheAttributes;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:39:50 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class CachesJcsMemory extends ACachesJcsImpl {
	public CachesJcsMemory() {
	}

	@Override
	public ECacheEngine getCacheEngine() {
		return ECacheEngine.JCS_MEMORY;
	}

	private static final Lock lockGet = new ReentrantReadWriteLock().writeLock();
	private final Map<String, JCS> jcsMap = new HashMap<String, JCS>(8);
	private JCS DEFAULT;

	@Override
	public JCS safeGetCache(final String cacheName, Properties props) {
		JCS jcsCache = jcsMap.get(cacheName);
		if (null != jcsCache)
			return jcsCache;
		lockGet.lock();
		try {
			jcsCache = jcsMap.get(cacheName);
			if (null != jcsCache)
				return jcsCache;
			if (null == props)
				props = ACaches.EMPTY_PROPS;
			if (null == DEFAULT)
				try {
					DEFAULT = JCS.getInstance("MC");
					try {
						DEFAULT.clear();
					} catch (final Exception ex) {
					}
				} catch (final CacheException e1) {
				}

			final CompositeCacheAttributes attrs = (CompositeCacheAttributes) (null == DEFAULT ? new CompositeCacheAttributes() : DEFAULT
					.getCacheAttributes().copy());
			attrs.setCacheName(cacheName);
			attrs.setMaxMemoryIdleTimeSeconds(ConvertUtils.toLong(props.getProperty("MaxMemoryIdleTimeSeconds"), 3600));
			attrs.setMaxObjects(ConvertUtils.toInt(props.getProperty("MaxObjects"), 10000000));
			attrs.setMemoryCacheName(props.getProperty("MemoryCacheName", "org.apache.jcs.engine.memory.lru.LRUMemoryCache"));
			attrs.setShrinkerIntervalSeconds(ConvertUtils.toLong(props.getProperty("ShrinkerIntervalSeconds"), 60));
			attrs.setUseDisk(ConvertUtils.toBoolean(props.getProperty("IsUseDisk"), true));
			attrs.setUseLateral(ConvertUtils.toBoolean(props.getProperty("IsLateral"), true));
			attrs.setUseRemote(ConvertUtils.toBoolean(props.getProperty("IsRemote"), false));
			try {
				jcsCache = JCS.getInstance(cacheName, attrs);
				try {
					jcsCache.clear();
				} catch (final Exception ex) {
				}
				jcsMap.put(cacheName, jcsCache);
			} catch (final CacheException e) {
				e.printStackTrace();//for debug
			}
		} finally {
			lockGet.unlock();
		}
		return jcsCache;
	}

	//used for lock operation when remove cache
	private final Lock lockDel = new ReentrantReadWriteLock().writeLock();

	@Override
	public void removeCache(final String cacheName) {
		try {
			lockDel.lock();
			final JCS cache = safeGetCache(cacheName, null);
			if (null == cache)
				return;
			try {
				cache.clear();
			} catch (final CacheException e) {
			} finally {
				cache.dispose();
			}
		} finally {
			lockDel.unlock();
		}
	}

	public static final String group = "all";

	@Override
	protected String getGroupId(final String cacheName) {
		return group;
	}
}
