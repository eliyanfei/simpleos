package net.itsite.utils;

import java.io.File;
import java.net.URL;

/**
 * @author QianFei.Xu;E-Mail:qianfei.xu@rosense.cn
 * @time Apr 26, 2009 7:11:06 PM
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
