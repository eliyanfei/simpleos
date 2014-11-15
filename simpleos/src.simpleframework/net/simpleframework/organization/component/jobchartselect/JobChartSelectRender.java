package net.simpleframework.organization.component.jobchartselect;

import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.tree.TreeRegistry;
import net.simpleframework.web.page.component.ui.tree.TreeRender;
import net.simpleframework.web.page.component.ui.window.WindowRender;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobChartSelectRender extends WindowRender {

	public JobChartSelectRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getJavascriptCode(compParameter));
		final TreeRender treeRender = (TreeRender) AbstractComponentRegistry.getRegistry(
				TreeRegistry.tree).getComponentRender();
		sb.append(treeRender.getJavascriptCode(ComponentParameter.get(compParameter,
				(AbstractComponentBean) compParameter.componentBean.getAttribute("__componentBean"))));
		return sb.toString();
	}
}
