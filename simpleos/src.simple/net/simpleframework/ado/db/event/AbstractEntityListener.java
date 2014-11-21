package net.simpleframework.ado.db.event;

import net.simpleframework.ado.db.IEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ALoggerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractEntityListener extends ALoggerAware implements IEntityListener {

	@Override
	public void beforeExecute(final IEntityManager manager, final SQLValue[] sqlValues) {
	}

	@Override
	public void afterExecute(final IEntityManager manager, final SQLValue[] sqlValues) {
	}
}
