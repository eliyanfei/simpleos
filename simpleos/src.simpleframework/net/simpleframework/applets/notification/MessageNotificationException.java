package net.simpleframework.applets.notification;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageNotificationException extends SimpleException {

	public MessageNotificationException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(MessageNotificationException.class, null, throwable);
	}

	private static final long serialVersionUID = -7942766511950281627L;
}
