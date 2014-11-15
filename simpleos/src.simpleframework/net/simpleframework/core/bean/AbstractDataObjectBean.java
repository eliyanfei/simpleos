package net.simpleframework.core.bean;

import net.simpleframework.core.AAttributeAware;
import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractDataObjectBean extends AAttributeAware implements IDataObjectBean {
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else {
			if (this instanceof IUniqueNameBeanAware) {
				final String name = ((IUniqueNameBeanAware) this).getName();
				if (name != null && obj instanceof IUniqueNameBeanAware) {
					return name.equals(((IUniqueNameBeanAware) obj).getName());
				} else {
					return super.equals(obj);
				}
			} else {
				return super.equals(obj);
			}
		}
	}

	@Override
	public int hashCode() {
		if (this instanceof IUniqueNameBeanAware) {
			final String name = ((IUniqueNameBeanAware) this).getName();
			return name == null ? super.hashCode() : name.hashCode();
		} else {
			return super.hashCode();
		}
	}

	public String getBooleanText(boolean v) {
		return v ? LocaleI18n.getMessage("common.yes") : LocaleI18n.getMessage("common.no");
	}
}
