package net.simpleos.utils;

import java.lang.reflect.Modifier;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SampleSubInstanceFilter extends SampleSubTypeFilter {
	public static final ISubTypeFilter DEFAULT = new SampleSubInstanceFilter();

	public SampleSubInstanceFilter() {
	}

	@Override
	protected boolean filterForInstance(final Class<?> clazz) {
		final int modifier = clazz.getModifiers();
		return (!Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier));
	}
}
