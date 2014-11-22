package net.simpleos.utils;

import net.simpleframework.core.id.ID;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
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
