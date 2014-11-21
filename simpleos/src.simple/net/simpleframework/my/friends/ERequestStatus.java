package net.simpleframework.my.friends;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum ERequestStatus {
	request {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("ERequestStatus.0");
		}
	},

	yes {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("ERequestStatus.1");
		}
	},

	no {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("ERequestStatus.2");
		}
	},

	ok
}
