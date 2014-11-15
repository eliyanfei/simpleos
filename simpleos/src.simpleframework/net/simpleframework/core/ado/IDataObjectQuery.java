package net.simpleframework.core.ado;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.simpleframework.ado.db.SQLValue;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDataObjectQuery<T> extends IDataObjectListenerAware {
	void move(final int first);

	void setSqlValue(SQLValue sqlValue);

	int position();

	T next();

	int getCount();

	void setCount(int count);

	int getFetchSize();

	void setFetchSize(int fetchSize);

	void reset();

	void close();

	public abstract static class Utils {

		public static <T> Enumeration<T> toEnumeration(final IDataObjectQuery<T> dataQuery) {
			return new Enumeration<T>() {
				private T t;

				@Override
				public boolean hasMoreElements() {
					return (t = dataQuery.next()) != null;
				}

				@Override
				public T nextElement() {
					return t;
				}
			};
		}

		public static <T> List<T> toList(final IDataObjectQuery<T> dataQuery) {
			T t;
			final List<T> al = new ArrayList<T>();
			if (dataQuery != null)
				while ((t = dataQuery.next()) != null) {
					al.add(t);
				}
			return al;
		}
	}
}
