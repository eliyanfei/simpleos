package net.simpleframework.core.ado;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DataObjectException extends SimpleException {
	public DataObjectException(final String msg) {
		super(msg);
	}

	public DataObjectException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(DataObjectException.class, msg);
	}

	public static RuntimeException wrapException(final Throwable cause) {
		return wrapException(DataObjectException.class, null, cause);
	}

	private static final long serialVersionUID = -539640491680179667L;
}
