package net.itsite.utils;

/**
 * @author QianFei.Xu;E-Mail:qianfei.xu@rosense.cn
 * @time Apr 26, 2009 7:07:52 PM
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
