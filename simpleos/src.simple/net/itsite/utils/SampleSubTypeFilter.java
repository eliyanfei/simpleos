package net.itsite.utils;

import java.net.URL;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SampleSubTypeFilter implements ISubTypeFilter {
	public static final ISubTypeFilter DEFAULT = new SampleSubTypeFilter();

	public SampleSubTypeFilter() {
	}

	@Override
	public boolean accept(final Class<?> baseType, final URL pathUrl, final String typePath) {
		String typeDef = typePath.replace('/', '.');
		typeDef = typeDef.substring(0, typeDef.lastIndexOf('.'));
		if (typeDef.equals(baseType.getName()))
			return false;
		Class<?> clazz;
		try {
			clazz = Class.forName(typeDef, false, Thread.currentThread().getContextClassLoader());
		} catch (final Throwable e) {
			return false;
		}
		if (!filterForInstance(clazz))
			return false;

		if (baseType.isAssignableFrom(clazz))
			return true;
		return false;
	}

	/**
	 * @param clazz
	 * @return
	 */
	protected boolean filterForInstance(final Class<?> clazz) {
		return true;
	}
}
