package net.simpleos.commons.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:39:09 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public abstract class ACaches {
	public abstract ECacheEngine getCacheEngine();

	/**
	 * 移除缓存实例
	 * 
	 * @param cacheName
	 */
	public abstract void removeCache(String cacheName);

	/**
	 * 获得某一种类型的Cache的Key列表
	 * 
	 * @param cacheName
	 * @return
	 */
	public abstract Collection<String> keysCache(String cacheName);

	static final Properties EMPTY_PROPS = new Properties();

	/**
	 * 获得一个缓存实例
	 * 
	 * @param cacheName
	 * @return
	 */
	public abstract Object safeGetCache(String cacheName, final Properties props);

	/**
	 * 获得一个缓存中的对象,不存在时将返回空
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	public abstract Object getCacheValue(String cacheName, String cacheKey);

	/**
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	public abstract Object removeCacheValue(String cacheName, String cacheKey);

	/**
	 * 允许手动添加一个缓存数据对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @param cacheObject
	 */
	public abstract void setCacheValue(final String cacheName, final String cacheKey, final Object cacheObject);

	/**
	 * 获得给定缓存类型和实际对象Key对应的缓存数据,只是一个String对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	public final String getStringValue(final String cacheName, final String cacheKey) {
		final Object tmpV = getCacheValue(cacheName, cacheKey);
		return null == tmpV ? null : tmpV.toString();
	}

	public final String getStringValueRemove(final String cacheName, final String cacheKey) {
		final Object tmpV = getCacheValue(cacheName, cacheKey);

		return null == tmpV ? null : tmpV.toString();
	}

	/**
	 * 获得给定缓存类型和实际对象Key对应的缓存数据,只是一个Map&lt;String,String&gt;对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final Map<String, String> getMapValue(final String cacheName, final String cacheKey) {
		return (Map<String, String>) getCacheValue(cacheName, cacheKey);
	}

	/**
	 * 获得给定缓存类型和实际对象Key对应的缓存数据,只是一个Map&lt;String,Map&lt;String,String&gt;&gt;对象
	 * 
	 * @param cacheName
	 * @param cacheKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final Map<String, Map<String, String>> getMapMapValue(final String cacheName, final String cacheKey) {
		return (Map<String, Map<String, String>>) getCacheValue(cacheName, cacheKey);
	}
}