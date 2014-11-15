package net.simpleframework.content;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EContentStatus {
	edit {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentStatus.0");
		}
	},

	audit {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentStatus.1");
		}
	},

	publish {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentStatus.2");
		}
	},

	lock {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentStatus.3");
		}
	},

	delete {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentStatus.4");
		}
	}
}
