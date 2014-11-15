package net.simpleframework.web.page.component.ui.list;

import net.simpleframework.web.page.component.AbstractComponentHtmlRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;

public class ListRender extends AbstractComponentHtmlRender {
	public ListRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/list_node.jsp?").append(ListUtils.BEAN_ID);
		sb.append("=").append(compParameter.componentBean.hashId());
		return sb.toString();
	}
}
