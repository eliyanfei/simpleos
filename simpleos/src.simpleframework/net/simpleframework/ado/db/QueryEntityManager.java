package net.simpleframework.ado.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class QueryEntityManager extends AbstractEntityManager implements IQueryEntityManager {
	public QueryEntityManager(final DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Map<String, Object> queryForMap(final String sql) {
		return queryForMap(new SQLValue(sql));
	}

	@Override
	public Map<String, Object> queryForMap(final SQLValue sqlValue) {
		return createQueryMap(null, sqlValue);
	}

	@Override
	public long queryForLong(final SQLValue value) {
		return executeQuery(value, new IQueryExtractor<Long>() {
			@Override
			public Long extractData(final ResultSet rs) throws SQLException {
				return rs.next() ? rs.getLong(1) : 0l;
			}
		});
	}

	@Override
	public int queryForInt(final SQLValue value) {
		return executeQuery(value, new IQueryExtractor<Integer>() {
			@Override
			public Integer extractData(final ResultSet rs) throws SQLException {
				return rs.next() ? rs.getInt(1) : 0;
			}
		});
	}

	@Override
	public IQueryEntitySet<Map<String, Object>> query(final SQLValue sqlValue) {
		return createQueryEntitySet(null, sqlValue);
	}

	@Override
	public IQueryEntitySet<Map<String, Object>> query(final String sql) {
		return query(new SQLValue(sql));
	}

	@Override
	public <T> IQueryEntitySet<T> query(final SQLValue value, final Class<T> beanClass) {
		return createQueryEntitySet(null, value, beanClass);
	}
}
