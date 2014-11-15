package net.simpleframework.web.page.parser;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ParserRuntimeException extends SimpleException {

	private static final long serialVersionUID = 5520927549351783305L;

	public ParserRuntimeException(final String msg) {
		super(msg);
	}

	public ParserRuntimeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(ParserRuntimeException.class, null, throwable);
	}
}
