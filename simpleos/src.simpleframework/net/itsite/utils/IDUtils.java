package net.itsite.utils;

import net.simpleframework.core.id.ID;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @Description：
 * @Company: 北京洛神科技有限公司
 * @author: 李岩飞
 * @Time: Apr 1, 2011 4:16:54 PM
 */
public final class IDUtils {

	public static boolean isBlank(final Object id) {
		if (id == null) {
			return true;
		}
		Object value = id;
		if (id instanceof ID) {
			value = ((ID) id).getValue();
		}
		return (StringUtils.isBlank(String.valueOf(value))) ? true : false;
	}

	public static boolean isNotBlank(final Object id) {
		return !isBlank(id);
	}
}
