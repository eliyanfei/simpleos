package net.itsite.cache;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

public class CachesJcsDisk extends ACachesJcsImpl {
	public CachesJcsDisk() {
	}

	@Override
	public ECacheEngine getCacheEngine() {
		return ECacheEngine.JCS_DISK;
	}

	private JCS dcCache;

	@Override
	public JCS safeGetCache(final String cacheName, final Properties props) {
		if (null != dcCache)
			return dcCache;
		try {
			dcCache = JCS.getInstance("DC");
		} catch (final CacheException e) {
		}
		return dcCache;
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
			final Collection<String> keys = keysCache(cacheName);
			for (final String key : keys)
				cache.remove(key, cacheName);
		} finally {
			lockDel.unlock();
		}
	}

	@Override
	protected String getGroupId(final String cacheName) {
		return cacheName;
	}
}
