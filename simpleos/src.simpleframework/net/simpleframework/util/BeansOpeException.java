package net.simpleframework.util;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BeansOpeException extends SimpleException {
	public BeansOpeException(final String msg) {
		super(msg);
	}

	public BeansOpeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(BeansOpeException.class, null, throwable);
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(BeansOpeException.class, msg, null);
	}

	private static final long serialVersionUID = 8998574687653782282L;
}
