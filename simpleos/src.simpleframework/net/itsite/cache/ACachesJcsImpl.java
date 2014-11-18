package net.itsite.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

abstract class ACachesJcsImpl extends ACaches {
	static {
		JCS.setConfigFilename("/net/itsite/cache/cache.ccf");
	}

	@Override
	public abstract JCS safeGetCache(final String cacheName, Properties props);

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> keysCache(final String cacheName) {
		final JCS cache = safeGetCache(cacheName, null);
		if (null == cache)
			return new HashSet<String>(0);
		return cache.getGroupKeys(getGroupId(cacheName));
	}

	@Override
	public Object getCacheValue(final String cacheName, final String cacheKey) {
		final JCS cache = safeGetCache(cacheName, null);
		if (null == cache)
			return null;
		return cache.getFromGroup(cacheKey, getGroupId(cacheName));
	}

	@Override
	public Object removeCacheValue(String cacheName, String cacheKey) {
		final JCS cache = safeGetCache(cacheName, null);
		if (null == cache)
			return null;
		Object obj = cache.getFromGroup(cacheKey, getGroupId(cacheName));
		cache.remove(cacheKey, getGroupId(cacheName));
		return obj;
	}

	//used for lock operation when set cache value
	private final Lock lockSet = new ReentrantReadWriteLock().writeLock();

	@Override
	public void setCacheValue(final String cacheName, final String cacheKey, final Object cacheObject) {
		lockSet.lock();
		try {
			final JCS cache = safeGetCache(cacheName, null);
			if (null == cache)
				return;
			try {
				cache.putInGroup(cacheKey, getGroupId(cacheName), cacheObject);
			} catch (final CacheException e) {
				e.printStackTrace();//for debug
			}
		} finally {
			lockSet.unlock();
		}
	}

	protected abstract String getGroupId(String cacheName);
}
