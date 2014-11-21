package net.simpleframework.organization;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrganizationException extends SimpleException {
	private static final long serialVersionUID = 6885538428759225872L;

	public OrganizationException(final String msg) {
		super(msg);
	}

	public OrganizationException(final String msg, final Throwable cause) {
		super(msg);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(OrganizationException.class, null, throwable);
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(OrganizationException.class, msg, null);
	}
}
