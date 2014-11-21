package net.simpleframework.web.page.component;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ComponentException extends SimpleException {

	private static final long serialVersionUID = 5936937563262430751L;

	public ComponentException(final String msg) {
		super(msg);
	}

	public ComponentException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapComponentException(final Throwable cause) {
		return wrapException(ComponentException.class, null, cause);
	}

	public static ComponentException getComponentRefException() {
		return new ComponentException("");
	}

	public static ComponentException getNullComponentNameException() {
		return new ComponentException("");
	}

	public static ComponentException getPageResourceProviderException() {
		return new ComponentException("Inconsistent page resource provider.");
	}

	public static ComponentException getNotRegisteredException(final String componentName) {
		return new ComponentException("\"" + componentName + "\" components not registered.");
	}

	public static ComponentException getRegisteredException(final String componentName) {
		return new ComponentException("\"" + componentName + "\" components have been registered.");
	}
}
