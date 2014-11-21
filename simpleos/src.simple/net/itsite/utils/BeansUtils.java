package net.itsite.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class BeansUtils extends net.simpleframework.util.BeanUtils {

	public BeansUtils() {
	}

	public static Map bean2Map(final Object bean, final boolean readAndWrite) {
		try {
			return toMap(bean, readAndWrite);
		} catch (final Exception e) {
		}
		return null;
	}

	public static void map2Bean(final Map map, final Object bean) {
		try {
			copyProperties(bean, map);
		} catch (final Exception e) {
		}
	}

	public static void copyProperties(final Object dest, final Object orig) {
		try {
			BeanUtils.copyProperties(dest, orig);
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
