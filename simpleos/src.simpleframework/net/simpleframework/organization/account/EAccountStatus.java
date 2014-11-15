package net.simpleframework.organization.account;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EAccountStatus {

	register {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EAccountStatus.0");
		}
	},

	locked {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EAccountStatus.1");
		}
	},

	normal {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EAccountStatus.2");
		}
	},

	delete {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EAccountStatus.3");
		}
	}
}
