package net.simpleframework.util.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.Iterator;

import javax.sql.DataSource;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.AbstractXmlDocument;
import net.simpleframework.core.Logger;
import net.simpleframework.core.Version;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.XMLUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DbCreator {
	static Logger logger = ALoggerAware.getLogger(DbCreator.class);

	static final String stateAttri = "state";

	public static void executeSql(final DataSource dataSource, final String filepath)
			throws IOException {
		executeSql(dataSource, DbCreatorCallback.defaultCallback, filepath);
	}

	public static void executeSql(final DataSource dataSource, final DbCreatorCallback callback,
			final String filepath) throws IOException {
		SqlScriptDocument document;
		try {
			document = new SqlScriptDocument(new File(filepath));
		} catch (final FileNotFoundException e) {
			logger.warn(e);
			return;
		}

		update(dataSource, callback, document);

		// 运行sql补丁
		final File[] patchArr = new File(document.configFile.getParentFile().getAbsolutePath()
				+ File.separator + "patch").listFiles();
		if (patchArr != null) {
			final Version version = document.getVersion();
			for (final File patchFile : patchArr) {
				document = new SqlScriptDocument(patchFile);
				if (document.getVersion().complies(version)) {
					update(dataSource, callback, document);
				}
			}
		}
	}

	static void update(final DataSource dataSource, final DbCreatorCallback callback,
			final SqlScriptDocument document) {
		if (callback != null) {
			callback.execute(document.getName(), document.getVersion(), document.getDescription());
		}
		Connection connection = null;
		Statement stat = null;
		try {
			final Iterator<?> it = document.sqlIterator();
			while (it.hasNext()) {
				final Element element = (Element) it.next();
				final String state = element.attributeValue(stateAttri);
				if (state != null && (state.equals("update") || state.equals("ignore"))) {
					continue;
				}
				if (connection == null) {
					connection = dataSource.getConnection();
					stat = connection.createStatement();
				}
				final String sqlText = element.getTextTrim();
				final String[] sqlArr = StringUtils.split(sqlText,
						StringUtils.text(element.attributeValue("delimiter"), ";"));
				if (sqlArr != null && sqlArr.length > 0) {
					try {
						final long l = System.currentTimeMillis();
						for (final String sql : sqlArr) {
							stat.execute(sql);
						}
						if (callback != null) {
							callback.execute(sqlText, System.currentTimeMillis() - l, null,
									element.elementTextTrim("description"));
						}
						element.addAttribute(stateAttri, "update");
					} catch (final SQLException e) {
						System.out.println(sqlText);
						if (callback != null) {
							callback.execute(sqlText, 0, e, element.elementTextTrim("description"));
						}
						element.addAttribute(stateAttri, e instanceof SQLSyntaxErrorException ? "ignore"
								: "exception");
					}
				}
			}
			if (connection != null && !connection.getAutoCommit()) {
				connection.commit();
			}
		} catch (final SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (stat != null) {
					stat.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (final SQLException e) {
			}
		}
		document.saveToFile();
	}

	static class SqlScriptDocument extends AbstractXmlDocument {
		final File configFile;

		public SqlScriptDocument(final File configFile) throws FileNotFoundException {
			super(new FileInputStream(configFile));
			this.configFile = configFile;
		}

		void saveToFile() {
			try {
				XMLUtils.saveToFile(document, configFile);
			} catch (final IOException e) {
				logger.warn(e);
			}
		}

		String getName() {
			return getRoot().elementTextTrim("name");
		}

		Version getVersion() {
			return Version.getVersion(getRoot().elementTextTrim("version"));
		}

		String getDescription() {
			return getRoot().elementTextTrim("description");
		}

		Iterator<?> sqlIterator() {
			return getRoot().elementIterator("tran-sql");
		}
	}
}
