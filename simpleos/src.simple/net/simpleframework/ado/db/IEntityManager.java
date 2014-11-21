package net.simpleframework.ado.db;

import javax.sql.DataSource;

import net.simpleframework.ado.db.event.IEntityListener;
import net.simpleframework.core.ado.IDataObjectManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IEntityManager extends IDataObjectManager {
	DataSource getDataSource();

	<T> T execute(IConnectionAware<T> connection);

	/* query */
	<T> T executeQuery(SQLValue value, IQueryExtractor<T> extractor, int resultSetType);

	<T> T executeQuery(SQLValue value, IQueryExtractor<T> extractor);

	/* update */
	int execute(SQLValue[] sqlValues, IEntityListener l);

	int execute(SQLValue[] sqlValues);

	int execute(SQLValue sqlValue, IEntityListener l);

	int execute(SQLValue sqlValue);

	int executeTransaction(SQLValue[] sqlValues, IEntityListener l);

	int executeTransaction(SQLValue[] sqlValues);

	int executeTransaction(SQLValue sqlValue, IEntityListener l);

	int executeTransaction(SQLValue sqlValue);

	int[] batchUpdate(String[] sql);

	int[] batchUpdate(String sql, int batchCount, IBatchValueSetter setter);
}
