package net.simpleframework.core.id;

import net.simpleframework.util.ConvertUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractID<T extends Comparable<T>> implements ID, Comparable<AbstractID<T>> {
	protected T id;

	@Override
	public T getValue() {
		return id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (id != null && obj instanceof ID) {
			return id.equals(((ID) obj).getValue());
		}
		return super.equals(obj);
	}

	@Override
	public boolean equals2(final Object obj) {
		if (id != null) {
			final Object v = obj instanceof ID ? ((ID) obj).getValue() : obj;
			if (id instanceof Number) {
				return ((Number) id).longValue() == ConvertUtils.toLong(v, Long.MIN_VALUE);
			} else if (v instanceof Number) {
				return ((Number) v).longValue() == ConvertUtils.toLong(id, Long.MIN_VALUE);
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return ConvertUtils.toString(id);
	}

	@Override
	public int compareTo(final AbstractID<T> o) {
		return id.compareTo(o.id);
	}
}
