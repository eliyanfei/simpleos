package net.simpleframework.content;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EContentType {
	normal {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentType.0");
		}
	},

	recommended {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentType.1");
		}
	},

	announce {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentType.2");
		}
	},

	image {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EContentType.3");
		}
	};

	public static EContentType whichOne(String name) {
		final EContentType[] types = EContentType.values();
		for (EContentType type : types) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		return null;
	}
}
