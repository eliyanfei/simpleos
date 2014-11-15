package net.simpleframework.web.page.component.ui.pager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EExportFileType {
	csv {
		@Override
		public String toString() {
			return "CSV";
		}
	},

	excel {
		@Override
		public String toString() {
			return "Excel";
		}
	}
}
