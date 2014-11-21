package net.simpleframework.core;

import org.apache.commons.logging.Log;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public final class Logger {
	private final Log logger;

	public Logger(final Log logger) {
		this.logger = logger;
	}

	public void error(final Throwable th) {
		logger.error(null, th);
	}

	public void warn(final Throwable th) {
		logger.warn(null, th);
	}

	public void warn(final String message) {
		logger.warn(message);
	}

	public void info(final String message) {
		logger.info(message);
	}

	public void debug(final String message) {
		logger.debug(message);
	}
}
