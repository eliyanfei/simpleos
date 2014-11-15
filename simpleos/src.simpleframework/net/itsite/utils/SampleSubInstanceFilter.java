package net.itsite.utils;

import java.lang.reflect.Modifier;

/**
 * @author QianFei.Xu;E-Mail:qianfei.xu@rosense.cn
 * @time Apr 26, 2009 7:31:39 PM
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
