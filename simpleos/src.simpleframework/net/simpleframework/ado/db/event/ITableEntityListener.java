package net.simpleframework.ado.db.event;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ITableEntityListener extends IEntityListener {

	/* delete event */

	void beforeDelete(ITableEntityManager manager, IDataObjectValue dataObjectValue);

	void afterDelete(ITableEntityManager manager, IDataObjectValue dataObjectValue);

	/* insert event */

	void beforeInsert(ITableEntityManager manager, Object[] objects);

	void afterInsert(ITableEntityManager manager, Object[] objects);

	/* update event */

	void beforeUpdate(ITableEntityManager manager, Object[] objects);

	void afterUpdate(ITableEntityManager manager, Object[] objects);
}
