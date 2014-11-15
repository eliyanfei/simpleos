package net.itsite.document.docu;

import net.simpleframework.util.LocaleI18n;

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