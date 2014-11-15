package net.simpleframework.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.LogFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ALoggerAware {

	protected final Logger logger = getLogger(getClass());

	static Map<Class<?>, Logger> loggerMap = new ConcurrentHashMap<Class<?>, Logger>();

	public static Logger getLogger(final Class<?> clazz) {
		Logger logger = loggerMap.get(clazz);
		if (logger == null) {
			loggerMap.put(clazz, logger = new Logger(LogFactory.getLog(clazz)));
		}
		return logger;
	}
}
