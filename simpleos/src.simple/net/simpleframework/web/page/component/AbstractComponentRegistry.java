package net.simpleframework.web.page.component;

import javax.servlet.ServletContext;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.script.IScriptEval;
import net.simpleframework.web.page.AbstractPageSupport;
import net.simpleframework.web.page.IPageResourceProvider;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageResourceProviderRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractComponentRegistry extends AbstractPageSupport implements
		IComponentRegistry {
	public static AbstractComponentRegistry getRegistry(final String name) {
		return (AbstractComponentRegistry) ComponentRegistryFactory.getInstance()
				.getComponentRegistry(name);
	}

	public AbstractComponentRegistry(final ServletContext servletContext) {
		super(servletContext);
		LocaleI18n.addBasename(AbstractComponentRegistry.class);

		final Class<?> c = getClass();
		if (c.getClassLoader().getResourceAsStream(
				BeanUtils.getResourceClasspath(c, "message_" + LocaleI18n.getLocale() + ".properties")) != null) {
			LocaleI18n.addBasename(c);
		}
	}

	@Override
	public String getComponentDeploymentName() {
		return getComponentName();
	}

	protected abstract Class<? extends AbstractComponentBean> getBeanClass();

	protected abstract Class<? extends AbstractComponentRender> getRenderClass();

	protected Class<? extends AbstractComponentResourceProvider> getResourceProviderClass() {
		return DefaultComponentResourceProvider.class;
	}

	private IComponentRender render;

	@Override
	public IComponentRender getComponentRender() {
		if (render == null) {
			try {
				render = getRenderClass().getConstructor(IComponentRegistry.class).newInstance(this);
			} catch (final Exception e) {
				throw new ComponentException(null, e);
			}
		}
		return render;
	}

	private IComponentResourceProvider provider;

	@Override
	public IComponentResourceProvider getComponentResourceProvider() {
		if (provider == null) {
			try {
				final Class<?> c = getResourceProviderClass();
				if (c == null) {
					return null;
				}
				provider = getResourceProviderClass().getConstructor(IComponentRegistry.class)
						.newInstance(this);
			} catch (final Exception e) {
				throw new ComponentException(null, e);
			}
		}
		return provider;
	}

	@Override
	public IComponentResourceProvider getHomePathAware() {
		return getComponentResourceProvider();
	}

	@Override
	public AbstractComponentBean createComponentBean(final PageParameter pageParameter,
			final Element component) {
		AbstractComponentBean componentBean;
		try {
			componentBean = getBeanClass().getConstructor(IComponentRegistry.class,
					PageDocument.class, Element.class).newInstance(this,
					pageParameter.getPageDocument(), component);
		} catch (final Exception e) {
			throw ComponentException.wrapComponentException(e);
		}
		final IScriptEval scriptEval = pageParameter.getScriptEval();
		if (scriptEval != null) {
			scriptEval.putVariable("bean", componentBean);
		}
		componentBean.parseElement(scriptEval);
		return componentBean;
	}

	@Override
	public IPageResourceProvider getPageResourceProvider() {
		final PageResourceProviderRegistry prpr = PageResourceProviderRegistry
				.getInstance(getServletContext());
		return prpr.getPageResourceProvider(null);
	}

	protected boolean isComponentInCache(final PageParameter pageParameter, final String name) {
		return ComponentBeanUtils.getComponentBeanInCache(pageParameter.getSession(),
				AbstractComponentBean.getComponentHashByName(pageParameter.getPageDocument(), name)) != null;
	}

	public static String getLoadingContent() {
		return LocaleI18n.getMessage("AbstractComponentRegistry.loadingContent.0");
	}
}
