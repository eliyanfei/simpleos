package net.simpleos.commons.cache;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午5:40:09 
 * @Description: 标识缓存的状态,未加载,正在加载,已经加载
 *
 */
public class CacheState {
	public int cacheCount;
	public ECacheState eCacheState;

	public CacheState(int cacheCount, ECacheState eCacheState) {
		this.eCacheState = eCacheState;
		this.cacheCount = cacheCount;
	}

	public CacheState() {
		this(0, ECacheState.loading);
	}

	public enum ECacheState {
		loading, loaded
	}
}
