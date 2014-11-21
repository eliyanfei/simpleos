package net.simpleframework.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class ArrayListEx<T extends ArrayListEx<T, M>, M> extends ArrayList<M> {

	public T append(final M... btns) {
		if (btns != null) {
			for (final M btn : btns) {
				add(btn);
			}
		}
		return (T) this;
	}

	public T appendAll(final Collection<M> coll) {
		if (coll != null) {
			addAll(coll);
		}
		return (T) this;
	}

	@Override
	public boolean add(final M element) {
		if (element == null) {
			return false;
		}
		return super.add(element);
	}

	@Override
	public void add(final int index, final M element) {
		if (element == null) {
			return;
		}
		super.add(index, element);
	}

	@Override
	public T clone() {
		return (T) super.clone();
	}
}
