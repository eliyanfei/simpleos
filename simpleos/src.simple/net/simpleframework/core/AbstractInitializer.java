package net.simpleframework.core;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractInitializer extends ALoggerAware implements IInitializer {
	private IApplication application;

	private boolean sync;

	@Override
	public IApplication getApplication() {
		return application;
	}

	@Override
	public void doInit(final IApplication application) {
		this.application = application;
		final Class<?>[] arr = getI18n();
		if (arr != null) {
			for (final Class<?> clazz : arr) {
				LocaleI18n.addBasename(clazz);
			}
		}
		for (final PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(getClass())) {
			final Method readMethod = BeanUtils.getReadMethod(descriptor);
			if (readMethod != null) {
				if (IApplicationModule.class.isAssignableFrom(readMethod.getReturnType())) {
					try {
						((IApplicationModule) readMethod.invoke(this)).init(this);
					} catch (final Exception e) {
						e.printStackTrace();
						logger.error(e);
					}
				}
			}
		}
	}

	protected Class<?>[] getI18n() {
		return null;
	}

	@Override
	public boolean isSync() {
		return sync;
	}

	public void setSync(final boolean sync) {
		this.sync = sync;
	}
}
