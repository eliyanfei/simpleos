package net.itsite.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class CacheConfig {
	private static final long serialVersionUID = 519308346941135978L;
	public Properties properties;
	public String name;
	public final Collection<String> cacheList = new ArrayList<String>(2);
	public final ECacheEngine cacheEngine;

	/**
	 * @param name
	 */
	public CacheConfig(final String name, final ECacheEngine cacheEngine) {
		this.name = name;
		this.cacheEngine = cacheEngine;
	}

	public final boolean isShared() {
		if (null == properties)
			return false;
		return !"false".equals(properties.getProperty("shared", "true"));
	}

	public String getName() {
		return name;
	}
}
