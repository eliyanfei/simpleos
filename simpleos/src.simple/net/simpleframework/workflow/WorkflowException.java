package net.simpleframework.workflow;

import net.simpleframework.core.SimpleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkflowException extends SimpleException {
	private static final long serialVersionUID = 7984366934401318860L;

	public WorkflowException(final String msg) {
		super(msg);
	}

	public WorkflowException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static RuntimeException wrapException(final String msg) {
		return wrapException(WorkflowException.class, msg);
	}

	public static RuntimeException wrapException(final Throwable throwable) {
		return wrapException(WorkflowException.class, null, throwable);
	}
}
