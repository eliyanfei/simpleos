package net.simpleframework.util.db;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.Version;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DbCreatorCallback extends ALoggerAware {
	public static DbCreatorCallback defaultCallback = new DbCreatorCallback();

	public void execute(final String name, final Version version, final String description) {
	}

	public void execute(final String sql, final long timeMillis, final Exception exception,
			final String description) {
		logger.info("execute: [" + sql + "]");
		logger.info(exception != null ? "Fail" : "Success");
	}
}
