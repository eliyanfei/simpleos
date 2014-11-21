package net.simpleframework.web.page.component;

import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipInputStream;

import net.simpleframework.util.BeanUtils;
import net.simpleframework.web.page.AbstractResourceProvider;
import net.simpleframework.web.page.IPageResourceProvider;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractComponentResourceProvider extends AbstractResourceProvider implements
		IComponentResourceProvider {
	private final IComponentRegistry componentRegistry;

	public AbstractComponentResourceProvider(final IComponentRegistry componentRegistry) {
		super(componentRegistry.getServletContext());
		this.componentRegistry = componentRegistry;
	}

	@Override
	public String[] getDependentComponents(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return null;
	}

	@Override
	public String[] getPageJavascriptPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return null;
	}

	@Override
	public String[] getPageCssPath(final PageRequestResponse requestResponse,
			final Collection<AbstractComponentBean> componentBeans) {
		return null;
	}

	private String resourceHomePath;

	@Override
	public String getResourceHomePath() {
		if (resourceHomePath == null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(getPageResourceProvider().getResourceHomePath());
			if (!"/".equals(sb.charAt(sb.length() - 1))) {
				sb.append("/");
			}
			sb.append(getComponentRegistry().getComponentDeploymentName());
			resourceHomePath = sb.toString();
		}
		return resourceHomePath;
	}

	@Override
	public IComponentRegistry getComponentRegistry() {
		return componentRegistry;
	}

	@Override
	public IPageResourceProvider getPageResourceProvider() {
		return getComponentRegistry().getPageResourceProvider();
	}

	@Override
	public ZipInputStream getRequiredResource() throws IOException {
		return getZipInputStream(BeanUtils.getResourceClasspath(getComponentRegistry().getClass(),
				RESOURCE_NAME));
	}
}
