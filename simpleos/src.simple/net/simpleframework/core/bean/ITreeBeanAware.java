package net.simpleframework.core.bean;

import net.simpleframework.core.id.ID;
import net.simpleframework.util.BeansOpeException;
import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ITreeBeanAware extends IIdBeanAware {

	ID getParentId();

	void setParentId(final ID parentId);

	ITreeBeanAware parent();

	public static abstract class Utils {

		public static void assertParentId(final ITreeBeanAware tree) {
			final ID id = tree.getId();
			ITreeBeanAware p = tree.parent();
			while (p != null) {
				if (p.getId().equals(id)) {
					throw BeansOpeException.wrapException(LocaleI18n.getMessage("ITreeBeanAware.0"));
				}
				p = p.parent();
			}
		}
	}
}
