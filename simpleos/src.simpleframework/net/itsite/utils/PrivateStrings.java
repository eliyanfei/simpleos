package net.itsite.utils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
abstract class PrivateStrings {
	/**
	 * startsWith),�Խ�β(endsWith);
	 * 
	 * @param array
	 * @param valueItem
	 * @return
	 */
	public static final boolean likesIn(final String valueItem, final String[] array) {
		if (null == array || null == valueItem || "".equals(valueItem.trim()))
			return false;

		for (final String value : array) {
			if (valueItem.equals(value) || valueItem.startsWith(value) || valueItem.endsWith(value))
				return true;
		}
		return false;
	}

}
