package net.simpleframework.util.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class DbUtils {
	public static boolean isTableExists(final DataSource dataSource, final String tablename) throws SQLException {
		final Connection connection = dataSource.getConnection();
		try {
			return connection.getMetaData().getTables(null, null, tablename, new String[] { "TABLE" }).next();
		} finally {
			connection.close();
		}
	}

	// 判断 oracle sequence 是否已经存在
	public static boolean isSequenceExists(final DataSource dataSource, final String sequencename) throws SQLException {
		final Connection connection = dataSource.getConnection();
		PreparedStatement ps = null;
		try {
			final String sql = "select sequence_name from user_sequences where sequence_name='" + sequencename.toUpperCase() + "'";
			ps = connection.prepareStatement(sql);
			return ps.executeQuery().next();
		} finally {
			if (ps != null) {
				ps.close();
			}
			connection.close();
		}
	}

	public static String sqlEscape(final String aString) {
		if (aString == null) {
			return "";
		}
		if (aString.indexOf("'") == -1) {
			return aString;
		}
		final StringBuilder aBuffer = new StringBuilder(aString);
		int insertOffset = 0;
		for (int i = 0; i < aString.length(); i++) {
			if (aString.charAt(i) == '\'') {
				aBuffer.insert(i + insertOffset++, "'");
			}
		}
		return aBuffer.toString();
	}

	public static String trimSQL(final String sql) {
		return sql == null ? "" : sql.trim().replaceAll("  +", " ").replaceAll(" *, *", ",");
	}

	public static String getLocSelectSQL(final DataSource dataSource, String sql, final int i, final int fetchSize) {
		final String db = getDatabaseMetaData(dataSource).databaseProductName;
		final StringBuilder sb = new StringBuilder();
		if (db.equals(ISQLConstants.Oracle)) {
			sb.append("select * from (select ROWNUM as rn, t_orcl.* from (");
			sb.append(sql).append(") t_orcl) where rn > ").append(i).append(" and rn <= ").append(i + fetchSize);
			sql = sb.toString();
		} else if (db.equals(ISQLConstants.MySQL)) {
			sb.append(sql).append(" limit ");
			if (i == -1)
				sb.append(fetchSize);
			else
				sb.append(i).append(",").append(fetchSize);
			sql = sb.toString();
		} else if (db.equals(ISQLConstants.SQLServer)) {
		}
		return sql;
	}

	public static class _DatabaseMetaData {
		public String url;

		public String databaseProductName;
	}

	private static Map<DataSource, _DatabaseMetaData> databaseMetaDataMap = new ConcurrentHashMap<DataSource, _DatabaseMetaData>();

	public static _DatabaseMetaData getDatabaseMetaData(final DataSource dataSource) {
		_DatabaseMetaData _metaData = databaseMetaDataMap.get(dataSource);
		if (_metaData != null) {
			return _metaData;
		}
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			final DatabaseMetaData metaData = connection.getMetaData();
			_metaData = new _DatabaseMetaData();
			_metaData.databaseProductName = metaData.getDatabaseProductName();
			_metaData.url = metaData.getURL();
		} catch (final SQLException ex) {
			throw DatabaseException.wrapException(ex);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException e) {
				}
			}
		}
		databaseMetaDataMap.put(dataSource, _metaData);

		return _metaData;
	}

	public static String getIdsSQLParam(final String idColumnName, final int size) {
		final StringBuilder sb = new StringBuilder();
		sb.append(idColumnName);
		if (size == 1) {
			sb.append(" = ?");
		} else {
			sb.append(" in (");
			for (int i = 0; i < size; i++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append("?");
			}
			sb.append(")");
		}
		return sb.toString();
	}
}
