package net.simpleframework.web.page.component;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractPageSupport;
import net.simpleframework.web.page.AbstractUrlForward;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractComponentRender extends AbstractPageSupport implements
		IComponentRender {
	private final IComponentRegistry componentRegistry;

	public AbstractComponentRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry.getServletContext());
		this.componentRegistry = componentRegistry;
	}

	@Override
	public IComponentRegistry getComponentRegistry() {
		return componentRegistry;
	}

	@Override
	public IComponentResourceProvider getHomePathAware() {
		return getComponentRegistry().getComponentResourceProvider();
	}

	protected static void appendParameters(final StringBuilder sb,
			final ComponentParameter compParameter, final String uName) {
		// 优先级：requestData < selector < parameters
		sb.append(uName).append(" = ").append(uName).append(".addParameter(\"");
		final String includeRequestData = (String) compParameter
				.getBeanProperty("includeRequestData");
		sb.append(AbstractUrlForward.putRequestData(compParameter, includeRequestData))
				.append("\");");

		final AbstractComponentBean componentBean = compParameter.componentBean;
		sb.append(uName).append(" = ").append(uName).append(".addSelectorParameter(");
		sb.append(componentBean.getActionFunction()).append(".selector");
		final String selector = (String) compParameter.getBeanProperty("selector");
		if (StringUtils.hasText(selector)) {
			sb.append(" || \"").append(selector).append("\"");
		}
		sb.append(");");
		final String parameters = componentBean.getParameters();
		if (StringUtils.hasText(parameters)) {
			sb.append(uName).append(" = ").append(uName).append(".addParameter(\"");
			sb.append(parameters).append("\");");
		}
	}
}
