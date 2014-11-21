package net.simpleframework.web;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EFunctionModule {
	bbs() {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EFunctionModule.0");
		}
	},

	blog() {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EFunctionModule.1");
		}

		@Override
		public String getApplicationUrl() {
			return "/blog.html";
		}
	},

	news() {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EFunctionModule.2");
		}

		@Override
		public String getApplicationUrl() {
			return "/news.html";
		}
	},

	bbs_posts() {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EFunctionModule.3");
		}
	},

	blog_remark() {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EFunctionModule.4");
		}
	},

	news_remark() {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EFunctionModule.5");
		}
	},
	space_log() {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("EFunctionModule.6");
		}
	},
	docu() {
		@Override
		public String toString() {
			return "文档";
		}

		@Override
		public String getApplicationUrl() {
			return "/docu.html";
		}
	};

	public String getApplicationUrl() {
		return null;
	}
}
