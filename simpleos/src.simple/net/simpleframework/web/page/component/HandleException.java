package net.simpleframework.web.page.component;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HandleException extends SimpleException {
	private static final long serialVersionUID = 7623116181965540895L;

	public HandleException(final String msg) {
		super(msg);
	}

	public HandleException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(HandleException.class, msg);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(HandleException.class, null, throwable);
	}
}
