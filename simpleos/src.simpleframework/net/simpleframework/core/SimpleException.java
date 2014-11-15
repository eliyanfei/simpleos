package net.simpleframework.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import javax.servlet.ServletException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class SimpleException extends RuntimeException {

	public SimpleException(final String msg) {
		super(msg);
	}

	public SimpleException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static Throwable getCause(final Class<? extends Throwable> clazz,
			final Throwable throwable) {
		Throwable cause = throwable;
		while ((cause = cause.getCause()) != null && clazz.isAssignableFrom(cause.getClass())) {
			break;
		}
		return cause;
	}

	public static RuntimeException wrapException(final Class<? extends RuntimeException> exClazz,
			final String msg) {
		return wrapException(exClazz, msg, null);
	}

	public static RuntimeException wrapException(final Class<? extends RuntimeException> exClazz,
			final String msg, final Throwable throwable) {
		if (throwable == null) {
			try {
				return exClazz.getConstructor(String.class).newInstance(msg);
			} catch (final Exception e) {
			}
		}
		if (exClazz.isAssignableFrom(throwable.getClass())) {
			return (RuntimeException) throwable;
		} else {
			try {
				return exClazz.getConstructor(String.class, Throwable.class)
						.newInstance(msg, throwable);
			} catch (final Throwable e) {
			}
			return new RuntimeException(msg, throwable);
		}
	}

	public static Throwable convertThrowable(Throwable th) {
		if (th instanceof ServletException) {
			Throwable throwable = th;
			while (throwable != null && ServletException.class.isAssignableFrom(throwable.getClass())) {
				throwable = ((ServletException) throwable).getRootCause();
				if (throwable != null) {
					th = throwable;
				}
			}
		} else if (th instanceof UndeclaredThrowableException) {
			final Throwable throwable = ((UndeclaredThrowableException) th).getUndeclaredThrowable();
			if (throwable != null) {
				th = throwable;
			}
		} else if (th instanceof InvocationTargetException) {
			th = ((InvocationTargetException) th).getTargetException();
		}
		return th;
	}
}
