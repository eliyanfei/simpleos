package net.simpleframework.web.page.component.ui.list;

import net.simpleframework.core.SimpleException;
import net.simpleframework.util.LocaleI18n;

public class ListException extends SimpleException {

	private static final long serialVersionUID = 4785829455345144368L;

	public ListException(final String msg) {
		super(msg);
	}

	public static RuntimeException wrapDeleteException() {
		return wrapException(LocaleI18n.getMessage("TreeException.0"));
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(ListException.class, msg, null);
	}
}
