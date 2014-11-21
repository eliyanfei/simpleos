package net.simpleframework.my.message;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EMessageType {
	user {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EMessageType.0");
		}
	},

	broadcast {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EMessageType.2");
		}
	},

	notification {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EMessageType.1");
		}
	},

	project {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EMessageType.3");
		}
	},

	complaint {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("举报");
		}
	}
}
