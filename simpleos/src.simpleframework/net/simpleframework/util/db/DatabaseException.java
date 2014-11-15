package net.simpleframework.util.db;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DatabaseException extends SimpleException {

	public DatabaseException(final String msg) {
		super(msg);
	}

	public DatabaseException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final Throwable cause) {
		return wrapException(DatabaseException.class, null, cause);
	}

	private static final long serialVersionUID = -5965947412901004739L;
}
