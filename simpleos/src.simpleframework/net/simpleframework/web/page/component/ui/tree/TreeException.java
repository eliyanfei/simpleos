package net.simpleframework.web.page.component.ui.tree;

import net.simpleframework.core.SimpleException;
import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TreeException extends SimpleException {
	private static final long serialVersionUID = 220404294448529138L;

	public TreeException(final String msg) {
		super(msg);
	}

	public static RuntimeException wrapDeleteException() {
		return wrapException(LocaleI18n.getMessage("TreeException.0"));
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(TreeException.class, msg, null);
	}
}
