package net.simpleframework.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbCreator;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.util.db.DbUtils._DatabaseMetaData;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IInitializer extends IApplicationAware {

	boolean isSync();

	void doInit(final IApplication application);

	public static class Utils {

		public static String getApplicationDeployPath(final IApplication application,
				final String name) {
			String hp = application.getApplicationHomePath();
			if (!hp.endsWith("/")) {
				hp += "/";
			}
			return hp + name + "/";
		}

		public static void deployResource(final Class<?> rClazz, final IApplication application,
				final String name) {
			final InputStream inputStream = BeanUtils.getResourceRecursively(rClazz, "resource.zip");
			if (inputStream != null) {
				try {
					JavascriptUtils.unzipJsAndCss(
							inputStream,
							application.getApplicationAbsolutePath(application.getApplicationHomePath()
									+ "/" + name), application.getApplicationConfig().isResourceCompress());
				} catch (final IOException e) {
					throw ApplicationModuleException.wrapException(e);
				}
			}
		}

		public static void deploySqlScript(final Class<?> rClazz, final IApplication application,
				final String name) {
			if (!application.getApplicationConfig().isAutoDatabase()) {
				return;
			}
			final InputStream inputStream = BeanUtils.getResourceRecursively(rClazz, "sql-script.zip");
			if (inputStream != null) {
				final DataSource dataSource = application.getDataSource();
				final _DatabaseMetaData metaData = DbUtils.getDatabaseMetaData(dataSource);
				final String target = application.getApplicationAbsolutePath(application
						.getApplicationHomePath()
						+ "/"
						+ name
						+ "/sql-script/"
						+ StringUtils.hash(metaData.url));
				try {
					System.out.println(target);
					IoUtils.unzip(inputStream, target, false);
					DbCreator.executeSql(dataSource, target + File.separator
							+ metaData.databaseProductName.toLowerCase() + File.separator
							+ "sql-script.xml");
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
