package net.simpleframework.ado.db.event;

import net.simpleframework.ado.db.IEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.DataObjectException;
import net.simpleframework.core.ado.IDataObjectListener;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IEntityListener extends IDataObjectListener {

	void beforeExecute(IEntityManager manager, SQLValue[] sqlValues) throws DataObjectException;

	void afterExecute(IEntityManager manager, SQLValue[] sqlValues) throws DataObjectException;
}
