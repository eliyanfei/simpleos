package net.itsite.utils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class TypeFilter implements ITypeFilter {
	public static final ITypeFilter DEFAULT = new TypeFilter(".class");
	private String[] exts;

	public TypeFilter(final String... exts) {
		this.exts = exts;
	}

	@Override
	public boolean accept(final String typeName) {
		if (null == exts)
			return true;
		return PrivateStrings.likesIn(typeName, exts);
	}
}
