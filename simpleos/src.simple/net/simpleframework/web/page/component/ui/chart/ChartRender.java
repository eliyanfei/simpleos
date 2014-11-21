package net.simpleframework.web.page.component.ui.chart;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ChartRender extends AbstractComponentJavascriptRender {

	public ChartRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final ChartBean chartBean = (ChartBean) compParameter.componentBean;

		AbstractGraphTypeBean graphType;
		final IChartHandle chartHandle = (IChartHandle) compParameter.getComponentHandle();
		if (chartHandle != null) {
			graphType = chartHandle.getGraphType(compParameter);
		} else {
			graphType = chartBean.getGraphType();
		}
		if (graphType == null) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		final String actionFunc = chartBean.getActionFunction();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		sb.append(ComponentRenderUtils.initHeightWidth(compParameter));
		sb.append(actionFunc).append(".chart = Flotr.draw(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", ");
		sb.append(graphType.data()).append(", {");
		String s = chartBean.getTitle();
		if (StringUtils.hasText(s)) {
			sb.append("title: '").append(s).append("',");
		}
		s = chartBean.getSubtitle();
		if (StringUtils.hasText(s)) {
			sb.append("subtitle: '").append(s).append("',");
		}
		sb.append("grid: ").append(chartBean.getGrid().options()).append(",");
		sb.append("xaxis: ").append(chartBean.getXaxis().options()).append(",");
		sb.append("yaxis: ").append(chartBean.getYaxis().options());
		sb.append("});");
		sb.append(actionFunc).append(".draw = function() {");
		sb.append(actionFunc).append(".chart.restoreCanvas();");
		sb.append("};");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
