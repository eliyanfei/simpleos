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
public abstract class TableEntityAdapter extends AbstractEntityListener implements
		ITableEntityListener {

	@Override
	public void beforeInsert(final ITableEntityManager manager, final Object[] objects) {
	}

	@Override
	public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
	}

	@Override
	public void beforeUpdate(final ITableEntityManager manager, final Object[] objects) {
	}

	@Override
	public void afterUpdate(final ITableEntityManager manager, final Object[] objects) {
	}

	@Override
	public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
	}

	@Override
	public void beforeDelete(final ITableEntityManager manager,
			final IDataObjectValue dataObjectValue) {
	}
}
