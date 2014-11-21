package net.simpleframework.my.message;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum ENotification {
	bbsReply {
		@Override
		public String toString() {
			return LocaleI18n.getMessage("ENotification.0");
		}
	}
}
