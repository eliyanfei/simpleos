package net.simpleos.commons.mysql;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.simpleos.utils.StringsUtils;

import com.mysql.management.MysqldResource;

/**
 * 内置数据源
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午5:32:45 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public final class EmbedMySqlServer {
	private MysqldResource mysqlInstance;
	public final Properties props;

	private static String embedMySqlHome;

	public static String getPlatformBaseDir() {
		return System.getProperty("user.dir");
	}

	public static final void setEmbedMySqlHome(final String embedMySqlHome) {
		if (new File(embedMySqlHome + "/mysql-em").exists())
			EmbedMySqlServer.embedMySqlHome = embedMySqlHome;
		else
			EmbedMySqlServer.embedMySqlHome = getPlatformBaseDir();
	}

	public static final String getEmbedMySqlHome() {
		return null == embedMySqlHome ? getPlatformBaseDir() : embedMySqlHome;
	}

	public EmbedMySqlServer(final Properties props) {
		this.props = props;
	}

	private String port;

	public void startup() {
		final File baseDir = new File(getEmbedMySqlHome(), "mysql-em");
		mysqlInstance = new MysqldResource(baseDir);
		port = props.getProperty("port");
		if (StringsUtils.isBlank(port))
			props.put("port", port = String.valueOf((int) (Math.random() * 40000)));
		final Set<Object> keys = props.keySet();
		final Map<String, String> options = new HashMap<String, String>(keys.size());
		for (final Object key : keys) {
			final String val = props.getProperty(key.toString());
			if ("".equals(val))
				options.put(key.toString(), null);
			else
				options.put(key.toString(), val.replace("{$contextPath}", getPlatformBaseDir()));
		}
		if (!mysqlInstance.isRunning())
			mysqlInstance.start("PRJMGR_MySQL", options, false, keys.contains("defaults-file"));
	}

	public String getPort() {
		return port;
	}

	public boolean isRunning() {
		return null == mysqlInstance ? false : mysqlInstance.isRunning();
	}

	public void shutdown() {
		if (mysqlInstance != null)
			mysqlInstance.shutdown();
	}

	public void cleanup() {
		if (mysqlInstance != null)
			mysqlInstance.cleanup();
	}
}
