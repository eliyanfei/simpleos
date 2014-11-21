package net.simpleframework.ado.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import net.simpleframework.core.id.ID;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.util.db.ISQLConstants;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.SimpleNativeJdbcExtractor;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ResultSetUtils {
	private final static Map<DataSource, LobHandler> lobHandlers = new HashMap<DataSource, LobHandler>();

	public static LobHandler getLobHandler(final DataSource dataSource) {
		LobHandler lobHandler = lobHandlers.get(dataSource);
		if (lobHandler == null) {
			final String databaseProductName = DbUtils.getDatabaseMetaData(dataSource).databaseProductName;
			if (ISQLConstants.Oracle.equals(databaseProductName)) {
				lobHandlers.put(dataSource, lobHandler = new OracleLobHandler());
				final String className = dataSource.getClass().getName();
				NativeJdbcExtractor jdbcExtractor;
				if (className.startsWith("org.apache.commons.dbcp")) {
					jdbcExtractor = new CommonsDbcpNativeJdbcExtractor();
				} else if (className.startsWith("com.mchange.v2.c3p0")) {
					jdbcExtractor = new C3P0NativeJdbcExtractor();
				} else {
					jdbcExtractor = new SimpleNativeJdbcExtractor();
				}
				((OracleLobHandler) lobHandler).setNativeJdbcExtractor(jdbcExtractor);
			} else {
				lobHandlers.put(dataSource, lobHandler = new DefaultLobHandler());
			}
		}
		return lobHandler;
	}

	public static Object getResultSetValue(final DataSource dataSource, final ResultSet rs,
			final int columnIndex, final Class<?> requiredType) throws SQLException {
		Object obj = null;
		if (requiredType != null) {
			if (InputStream.class.isAssignableFrom(requiredType)) {
				obj = getLobHandler(dataSource).getBlobAsBinaryStream(rs, columnIndex);
			} else if (Reader.class.isAssignableFrom(requiredType)) {
				obj = getLobHandler(dataSource).getClobAsCharacterStream(rs, columnIndex);
			}
		} else {
			final int type = rs.getMetaData().getColumnType(columnIndex);
			if (type == Types.BINARY || type == Types.VARBINARY || type == Types.LONGVARBINARY
					|| type == Types.BLOB) {
				obj = getLobHandler(dataSource).getBlobAsBinaryStream(rs, columnIndex);
			} else if (type == Types.CLOB) {
				obj = getLobHandler(dataSource).getClobAsCharacterStream(rs, columnIndex);
			}
		}
		if (obj == null) {
			obj = JdbcUtils.getResultSetValue(rs, columnIndex, requiredType);
		}
		return obj;
	}

	public static int lookupColumnIndex(final ResultSetMetaData resultSetMetaData,
			final String columnName) throws SQLException {
		if (columnName != null) {
			final int columnCount = resultSetMetaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				if (columnName.equalsIgnoreCase(lookupColumnName(resultSetMetaData, i))) {
					return i;
				}
			}
		}
		return 0;
	}

	public static String lookupColumnName(final ResultSetMetaData resultSetMetaData,
			final int columnIndex) throws SQLException {
		return org.springframework.jdbc.support.JdbcUtils.lookupColumnName(resultSetMetaData,
				columnIndex);
	}

	public static Object[] getParameterValues(final DataSource dataSource, final Object[] values) {
		if (values == null) {
			return null;
		}
		final Object[] newValues = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] instanceof Enum<?>) {
				newValues[i] = ((Enum<?>) values[i]).ordinal();
			} else if (values[i] instanceof ID) {
				newValues[i] = ((ID) values[i]).getValue();
			} else {
				try {
					if (values[i] instanceof InputStream) {
						final InputStream inputStream = (InputStream) values[i];
						if (inputStream.available() == 0) {
							values[i] = null;
						} else {
							final Object lobValue = new SqlLobValue(inputStream, -1,
									getLobHandler(dataSource));
							values[i] = new SqlParameterValue(Types.BLOB, lobValue);
						}
					} else if (values[i] instanceof Reader) {
						final Object lobValue = new SqlLobValue((Reader) values[i], -1,
								getLobHandler(dataSource));
						values[i] = new SqlParameterValue(Types.CLOB, lobValue);
					}
				} catch (final IOException e) {
				}
				newValues[i] = values[i];
			}
		}
		return newValues;
	}
}
