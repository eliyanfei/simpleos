package net.itsite.docu;

import net.simpleframework.util.LocaleI18n;

/**
 * 文档状态
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:18:52 
 *
 */
public enum EDocuStatus {
	audit {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("Docu.status.0");
		}
	},
	edit {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("Docu.status.1");
		}
	},

	publish {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("Docu.status.2");
		}
	},

	delete_user {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("Docu.status.3");
		}
	},

	delete_manager {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("Docu.status.4");
		}
	}
}