package net.simpleframework.web.page;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageException extends SimpleException {
	private static final long serialVersionUID = 7838430650458772788L;

	public PageException(final String msg) {
		super(msg);
	}

	public PageException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(PageException.class, msg);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(PageException.class, null, throwable);
	}
}
