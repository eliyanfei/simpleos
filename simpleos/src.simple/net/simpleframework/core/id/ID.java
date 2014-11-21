package net.simpleframework.core.id;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ID extends java.io.Serializable {
	public static ID zero = new LongID(0);

	public static ID nullId = new NullID();

	Object getValue();

	boolean equals2(final Object obj);

	public static class Utils {
		public static boolean isNone(final ID id) {
			return id == null || id.equals2(zero) || id.equals2(nullId);
		}

		public static ID newID(final Object id) {
			return newID(id, id == null ? null : id.getClass());
		}

		public static ID newID(final Object id, final Class<?> idType) {
			if (id instanceof ID) {
				return (ID) id;
			}
			if (id == null) {
				return null;
			}
			if (Long.class.isAssignableFrom(idType)) {
				return new LongID(id);
			} else if (Number.class.isAssignableFrom(idType)) {
				return new IntegerID(id);
			} else {
				return new StringID(id);
			}
		}
	}
}
