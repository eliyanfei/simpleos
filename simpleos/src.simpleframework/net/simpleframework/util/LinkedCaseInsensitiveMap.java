package net.simpleframework.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * from spring
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class LinkedCaseInsensitiveMap<V> extends LinkedHashMap<String, V> {
	private final Map<String, String> caseInsensitiveKeys;

	private final Locale locale;

	public LinkedCaseInsensitiveMap() {
		this(null);
	}

	public LinkedCaseInsensitiveMap(final Locale locale) {
		super();
		this.caseInsensitiveKeys = new HashMap<String, String>();
		this.locale = (locale != null ? locale : Locale.getDefault());
	}

	public LinkedCaseInsensitiveMap(final int initialCapacity) {
		this(initialCapacity, null);
	}

	public LinkedCaseInsensitiveMap(final int initialCapacity, final Locale locale) {
		super(initialCapacity);
		this.caseInsensitiveKeys = new HashMap<String, String>(initialCapacity);
		this.locale = (locale != null ? locale : Locale.getDefault());
	}

	@Override
	public V put(final String key, final V value) {
		this.caseInsensitiveKeys.put(convertKey(key), key);
		return super.put(key, value);
	}

	@Override
	public boolean containsKey(final Object key) {
		return (key instanceof String && this.caseInsensitiveKeys
				.containsKey(convertKey((String) key)));
	}

	@Override
	public V get(final Object key) {
		if (key instanceof String) {
			return super.get(this.caseInsensitiveKeys.get(convertKey((String) key)));
		} else {
			return null;
		}
	}

	@Override
	public V remove(final Object key) {
		if (key instanceof String) {
			return super.remove(this.caseInsensitiveKeys.remove(convertKey((String) key)));
		} else {
			return null;
		}
	}

	@Override
	public void clear() {
		this.caseInsensitiveKeys.clear();
		super.clear();
	}

	protected String convertKey(final String key) {
		return key.toLowerCase(this.locale);
	}

	private static final long serialVersionUID = -7467743735579931453L;
}
