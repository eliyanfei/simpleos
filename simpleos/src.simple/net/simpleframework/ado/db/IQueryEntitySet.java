package net.simpleframework.ado.db;

import net.simpleframework.core.ado.IDataObjectQuery;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IQueryEntitySet<T> extends IDataObjectQuery<T> {

	IEntityManager getEntityManager();

	SQLValue getSqlValue();

	QueryDialect getQueryDialect();

	void setQueryDialect(QueryDialect queryDialect);

	int getResultSetType();

	void setResultSetType(int resultSetType);
}
