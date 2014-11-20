package net.itsite.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import net.simpleframework.util.ConvertUtils;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:39:43 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class CachesEhcache extends ACaches {
	private static final String ehcacheXml = "ehcache.xml";
	private final CacheManager cacheMgr;

	public CachesEhcache() {
		this(CachesEhcache.ehcacheXml);
	}

	public CachesEhcache(final String configXml) {
		cacheMgr = new File(configXml).exists() ? CacheManager.create(configXml) : CacheManager.getInstance();
	}

	@Override
	public ECacheEngine getCacheEngine() {
		return ECacheEngine.EHCACHE_DISK;
	}

	private static final Lock lockGet = new ReentrantReadWriteLock().writeLock();

	/**
	 * 获得cacheName对应的Cache对象, 如果没有获取到Cache实例.将以<BR>
	 * <code>
	 *  new Cache(cacheName, 100, MemoryStoreEvictionPolicy.LFU,
				true, null, false, 6000000, 0, true, 360000, null, null,
				3000000)
	 * </code><BR>
	 * 来创建一个默认的Cache
	 * 
	 * @param domain
	 * @param cacheName
	 * @return 此方法将保证返回的Cache实例不会为空
	 */
	@Override
	public final Cache safeGetCache(final String cacheName, Properties props) {
		Cache cache = cacheMgr.getCache(cacheName);
		if (null != cache)
			return cache;
		CachesEhcache.lockGet.lock();
		try {
			cache = cacheMgr.getCache(cacheName);
			if (null != cache)
				return cache;
			if (null == props)
				props = ACaches.EMPTY_PROPS;
			cache = new Cache(cacheName, ConvertUtils.toInt(props.getProperty("maxElementsInMemory", "100"), 100), MemoryStoreEvictionPolicy.LFU,
					true, props.getProperty("diskStorePath", null), false, ConvertUtils.toLong(props.getProperty("timeToLiveSeconds", "6000000"),
							6000000), ConvertUtils.toLong(props.getProperty("timeToIdleSeconds", "0"), 0), false, ConvertUtils.toLong(
							props.getProperty("diskExpiryThreadIntervalSeconds", "360000"), 360000), null, null, ConvertUtils.toInt(
							props.getProperty("maxElementsOnDisk", "3000000"), 3000000));
			//			cache.clearStatistics();
			//			cache.removeAll();
			cacheMgr.addCache(cache);
		} finally {
			CachesEhcache.lockGet.unlock();
		}
		return cache;
	}

	@Override
	public Object getCacheValue(final String cacheName, final String cacheKey) {
		final Element cacheEle = safeGetCache(cacheName, null).get(cacheKey);
		if (null == cacheEle)
			return null;
		return cacheEle.getObjectValue();
	}

	public Object removeCacheValue(final String cacheName, final String cacheKey) {
		final Element cacheEle = safeGetCache(cacheName, null).get(cacheKey);
		safeGetCache(cacheName, null).remove(cacheKey);
		if (null == cacheEle)
			return null;
		return cacheEle.getObjectValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> keysCache(final String cacheName) {
		final Cache cache = cacheMgr.getCache(cacheName);
		try {
			return cache.getKeys();
		} catch (final Exception ex) {
			final List<Object> keys = cache.getKeys();
			final List<String> result = new ArrayList<String>(keys.size());
			for (final Object key : keys) {
				result.add(key.toString());
			}
			return result;
		}
	}

	@Override
	public void removeCache(final String cacheName) {
		cacheMgr.removeCache(cacheName);
	}

	//used for lock operation when set cache value
	private final Lock lockSet = new ReentrantReadWriteLock().writeLock();

	@Override
	public void setCacheValue(final String cacheName, final String cacheKey, final Object cacheObject) {
		try {
			lockSet.lock();
			safeGetCache(cacheName, null).put(new Element(cacheKey, cacheObject));
		} finally {
			lockSet.unlock();
		}
	}
}