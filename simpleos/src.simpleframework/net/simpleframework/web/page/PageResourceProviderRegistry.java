package net.simpleframework.web.page;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import net.simpleframework.core.ALoggerAware;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PageResourceProviderRegistry extends ALoggerAware {

	private static PageResourceProviderRegistry registry;

	public static PageResourceProviderRegistry getInstance(final ServletContext servletContext) {
		if (registry == null) {
			registry = new PageResourceProviderRegistry(servletContext);
		}
		return registry;
	}

	private PageResourceProviderRegistry(final ServletContext servletContext) {
		try {
			providers.put(IPageConstants.DEFAULT_PAGE_RESOURCE_PROVIDER,
					PageUtils.pageContext.createPageResourceProvider());
		} catch (final Exception e) {
			logger.error(e);
		}
	}

	private final Map<String, IPageResourceProvider> providers = new ConcurrentHashMap<String, IPageResourceProvider>();

	public IPageResourceProvider getPageResourceProvider(final String name) {
		if (name != null) {
			final IPageResourceProvider provider = providers.get(name);
			if (provider != null) {
				return provider;
			}
		}
		return providers.get(IPageConstants.DEFAULT_PAGE_RESOURCE_PROVIDER);
	}

	public void regist(final IPageResourceProvider pageResourceProvider) {
		final String name = pageResourceProvider.getName();
		if (StringUtils.hasText(name)) {
			providers.put(name, pageResourceProvider);
		}
	}

	public void romove(final IPageResourceProvider pageResourceProvider) {
	}
}
