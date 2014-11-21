package net.simpleframework.core;

import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AAttributeAware extends ALoggerAware implements IAttributeAware {

	private final ReferenceMap attributes;

	public AAttributeAware() {
		this(AbstractReferenceMap.HARD, AbstractReferenceMap.HARD);
	}

	public AAttributeAware(final int key, final int value) {
		attributes = new ReferenceMap(key, value);
	}

	protected void clearAttribute() {
		attributes.clear();
	}

	@Override
	public Object getAttribute(final String key) {
		return attributes.get(key);
	}

	@Override
	public Object removeAttribute(final String key) {
		return attributes.remove(key);
	}

	@Override
	public void setAttribute(final String key, final Object value) {
		if (value == null) {
			attributes.remove(key);
		} else {
			attributes.put(key, value);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Enumeration<String> getAttributeNames() {
		return new Vector<String>(attributes.keySet()).elements();
	}
}
