package net.simpleframework.core.bean;

import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IIdBeanAware extends IDataObjectBean {

	ID getId();

	void setId(final ID id);

	boolean equals2(final IIdBeanAware idBean);

	/**
	 * 是否内置条目
	 * @return
	 */
	public boolean isBuildIn();

	public static class Utils {
		public static boolean equals(final IUniqueNameBeanAware oThis, final Object obj) {
			if (oThis == obj) {
				return true;
			} else {
				final String name = oThis.getName();
				if (name != null && obj instanceof IUniqueNameBeanAware) {
					return name.equals(((IUniqueNameBeanAware) obj).getName());
				} else {
					return oThis.equals(obj);
				}
			}
		}

		public static int hashCode(final IUniqueNameBeanAware oThis) {
			final String name = oThis.getName();
			return name == null ? oThis.hashCode() : name.hashCode();
		}
	}
}
