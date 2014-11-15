package net.simpleframework.core.ado;

import net.simpleframework.ado.db.SQLValue;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDataObjectQuery<T> extends AbstractDataObjectSupport implements IDataObjectQuery<T> {
	protected int i;

	protected int count;

	public AbstractDataObjectQuery() {
		reset();
	}

	@Override
	public int position() {
		return i;
	}

	@Override
	public void move(final int toIndex) {
		i = Math.max(toIndex, -1);
	}

	@Override
	public void reset() {
		move(-1);
		count = -1;
	}

	@Override
	public void setCount(final int count) {
		this.count = count;
	}

	@Override
	public void close() {
	}

	@Override
	public void setSqlValue(SQLValue sqlValue) {
	}
}
