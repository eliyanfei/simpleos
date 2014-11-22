package net.simpleos.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月20日 下午12:00:44 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public final class SQLUtils {

	public static boolean isOpen(DataSource ds) {
		try {
			Connection conn = ds.getConnection();
			closeAll(null, null, conn);
			return true;
		} catch (SQLException e) {
		}
		return false;
	}

	/**
	 * 统计表格的行数
	 * @throws SQLException
	 */
	public static int count(final DataSource dataSource, final String table) {
		int size = 0;
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			size = count(conn, table);
		} catch (SQLException e) {
		} finally {
			closeAll(null, null, conn);
		}
		return size;
	}

	/**
	 * 统计表格的行数
	 * @throws SQLException
	 */
	public static int count(final Connection conn, final String table) {
		int size = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			final String sql = "select count(*) from " + table;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
		} finally {
			closeAll(rs, ps, null);
		}
		return size;
	}

	public static void closeAll(final ResultSet rs, final Statement st, final Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (final Exception e) {
		}
	}

	public static String whereInSql(String column, final int size) {
		final StringBuffer exp = new StringBuffer();
		for (int i = 0; i < size; i++) {
			if (i == 0) {
				exp.append(column + "  in(?");
				continue;
			}
			exp.append(",?");
		}
		exp.append(")");
		return exp.toString();
	}

	/**
	 * 浠庝竴寮犺〃鎻掑叆鏁版嵁鍒板彟澶栦竴寮犺〃
	 * @param fromTable
	 * @param toTable
	 * @param jt
	 */
	public static void insertData(final String fromTable, final String toTable, final String exp, final JdbcTemplate jt) {
		Connection conn = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		try {
			conn = jt.getDataSource().getConnection();
			ps = conn.prepareStatement("SELECT * FROM " + fromTable + " where 1=1 " + exp);
			rs = ps.executeQuery();
			final ResultSetMetaData metaData = rs.getMetaData();
			final int colsLen = metaData.getColumnCount() + 1;
			final StringBuffer buf = new StringBuffer(20);
			buf.append("INSERT INTO ").append(toTable).append("(");
			final StringBuffer value = new StringBuffer(10);
			for (int i = 1; i < colsLen; i++) {
				buf.append(metaData.getColumnLabel(i));
				value.append("?");
				if (i != colsLen - 1) {
					buf.append(",");
					value.append(",");
				}
			}
			buf.append(") VALUES(").append(value).append(")");
			ps1 = conn.prepareStatement(buf.toString());
			while (rs.next()) {
				for (int i = 1; i < colsLen; i++) {
					ps1.setObject(i, rs.getObject(metaData.getColumnLabel(i)));
				}
				ps1.addBatch();
			}
			ps1.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(rs, ps1, null);
			closeAll(null, ps, conn);
		}
	}

	public static List<String> listLabels(final String sql, final JdbcTemplate jt) {
		final List<String> result = new ArrayList<String>();
		jt.query(sql, new ResultSetExtractor<Object>() {

			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				final ResultSetMetaData md = rs.getMetaData();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					result.add(md.getColumnLabel(i));
				}
				return null;
			}
		});

		return result;
	}

	public static <T> List<T> listValues(final String sql, final JdbcTemplate jt, final Class<T> t) {
		final List<T> valueList = new ArrayList<T>();
		jt.query(sql, new ResultSetExtractor<Object>() {

			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				final String cName = t.getCanonicalName();
				final boolean isTimestamp = "java.sql.Timestamp".equals(cName);
				final boolean isDouble = "java.lang.Double".equals(cName);
				final boolean isInteger = "java.lang.Integer".equals(cName);
				final boolean isFloat = "java.lang.Float".equals(cName);
				final boolean isShot = "java.lang.Short".equals(cName);
				final boolean isLong = "java.lang.Long".equals(cName);
				final boolean isBigDecimal = "class java.math.BigDecimal".equals(cName);
				final boolean isString = "java.lang.String".equals(cName);
				Object value;
				while (rs.next()) {
					if (isTimestamp)
						value = rs.getTimestamp(1);
					else if (isString) {
						value = StringsUtils.trimNull(rs.getString(1));
					} else if (isDouble)
						value = rs.getDouble(1);
					else if (isInteger)
						value = rs.getInt(1);
					else if (isFloat)
						value = rs.getFloat(1);
					else if (isShot)
						value = rs.getShort(1);
					else if (isLong)
						value = rs.getLong(1);
					else if (isBigDecimal)
						value = rs.getBigDecimal(1);
					else
						value = rs.getObject(1);
					if (null == value)
						value = "";
					valueList.add(t.cast(value));
				}
				return null;
			}

		});
		return valueList;
	}
}