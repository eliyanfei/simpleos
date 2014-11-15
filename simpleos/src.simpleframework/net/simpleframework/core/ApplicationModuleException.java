package net.simpleframework.core;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ApplicationModuleException extends SimpleException {
	private static final long serialVersionUID = 1071482938350856030L;

	public ApplicationModuleException(final String msg) {
		super(msg);
	}

	public ApplicationModuleException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(ApplicationModuleException.class, null, throwable);
	}
}
