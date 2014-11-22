package net.simpleos.utils;

import java.io.File;
import java.net.URL;
/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class PathURLFilter implements IPathURLFilter {
	public static final IPathURLFilter DEFAULT = new PathURLFilter("/", ".jar");
	private String[] exts;

	public PathURLFilter(final String... exts) {
		this.exts = exts;
	}

	@Override
	public boolean accept(final URL pathURL) {
		// for any
		if (null == exts)
			return true;
		// do filter
		return PrivateStrings.likesIn(new File(pathURL.getFile()).getName(), exts);
	}
}
