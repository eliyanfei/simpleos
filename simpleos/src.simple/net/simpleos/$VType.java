package net.simpleos;

import net.simpleframework.core.Version;
import net.simpleframework.util.LocaleI18n;

/**
 * 类型，企业版或者免费版
 * 
 * @author 李岩飞 2013-3-26下午04:35:32
 */
public enum $VType {
	free(PrjVersion.latest) {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("VType.1");
		}
	};
	public Version ver;

	private $VType(Version ver) {
		this.ver = ver;
	}

	public static $VType getNowVType() {
		return free;
	}
}
