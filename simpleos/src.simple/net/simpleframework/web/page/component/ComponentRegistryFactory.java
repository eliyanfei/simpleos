package net.simpleframework.web.page.component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.web.page.IPageConstants;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public final class ComponentRegistryFactory extends ALoggerAware {
	private static ComponentRegistryFactory instance;

	public static synchronized ComponentRegistryFactory getInstance() {
		if (instance == null) {
			instance = new ComponentRegistryFactory();
		}
		return instance;
	}

	private final Map<String, IComponentRegistry> components = new ConcurrentHashMap<String, IComponentRegistry>();

	public IComponentRegistry getComponentRegistry(final String componentName) {
		return components.get(componentName);
	}

	public void regist(final IComponentRegistry componentRegistry) {
		final String componentName = componentRegistry.getComponentName();
		if (components.containsKey(componentName)) {
			throw ComponentException.getRegisteredException(componentName);
		}

		try {
			//			ComponentResourceProviderUtils.extract(componentRegistry);

			final IComponentResourceProvider provider = componentRegistry.getComponentResourceProvider();
			if (provider != null) {
				final String[] jarPath = provider.getJarPath();
				if (jarPath != null) {
					final int length = jarPath.length;
					if (length > 0) {
						final URL[] urls = new URL[length];
						for (int i = 0; i < length; i++) {
							final String realPath = componentRegistry.getServletContext().getRealPath(provider.getResourceHomePath() + jarPath[i]);
							urls[i] = new URL(IPageConstants.PROTOCAL_FILE_PREFIX + realPath);
						}
						loadJarFiles(urls);
					}
				}
			}
		} catch (final IOException e) {
			throw ComponentException.wrapComponentException(e);
		}
		components.put(componentName, componentRegistry);
	}

	private void loadJarFiles(final URL[] urls) {
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (!(cl instanceof URLClassLoader)) {
				cl = ClassLoader.getSystemClassLoader();
			}
			if (cl instanceof URLClassLoader) {
				final Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
				method.setAccessible(true);
				for (final URL url : urls) {
					method.invoke(cl, new Object[] { url });
				}
			}
		} catch (final Exception e) {
			logger.warn(e);
		}
	}

	public void remove(final IComponentRegistry componentRegistry) {
	}
}
