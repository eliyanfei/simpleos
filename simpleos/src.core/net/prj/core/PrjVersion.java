package net.prj.core;

import net.simpleframework.core.Version;

/**
 * 项目开发的版本
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-11-29上午11:25:56
 */
public class PrjVersion {
	/**
	 * 判断给定的数据版本信息是否低于当前定义的latest版本号
	 */
	public static final boolean isOlder(final Version productVer) {
		return productVer.less(latest);
	}

	//企业版更新内容
	public static final Version V1_0_0 = Version.valueOf("1.0.0");
	public static final Version V1_1_5 = Version.valueOf("1.1.5");
	public static final Version V1_1_7 = Version.valueOf("1.1.7");

	public static final Version latest = V1_1_7;//解析系统版本

	/**
	 * 获得版本号
	 */
	public static String $version() {
		return "V" + latest;
	}
}
