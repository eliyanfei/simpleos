package net.simpleframework.web.page.component.ui.progressbar;

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
public class ProgressBarRender extends AbstractComponentJavascriptRender {
	public ProgressBarRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		final String actionFunc = compParameter.componentBean.getActionFunction();
		sb.append(actionFunc).append(".progressbar = new $Comp.ProgressBar(")
				.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		sb.append("\"url\": \"").append(getResourceHomePath(compParameter));
		sb.append("/jsp/progressbar.jsp?").append(ProgressBarUtils.BEAN_ID).append("=");
		sb.append(compParameter.componentBean.hashId()).append("\",");
		sb.append("\"maxProgressValue\": ");
		sb.append(compParameter.getBeanProperty("maxProgressValue")).append(",");
		sb.append("\"step\": ");
		sb.append(compParameter.getBeanProperty("step")).append(",");
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		final int detailHeight = (Integer) compParameter.getBeanProperty("detailHeight");
		if (detailHeight > 0) {
			sb.append("\"detailHeight\": \"").append(detailHeight).append("px\",");
		}
		sb.append("\"interval\": ");
		sb.append(compParameter.getBeanProperty("interval")).append(",");
		sb.append("\"startAfterCreate\": ").append(compParameter.getBeanProperty("startAfterCreate"))
				.append(",");
		sb.append("\"showAbortAction\": ").append(compParameter.getBeanProperty("showAbortAction"))
				.append(",");
		sb.append("\"showDetailAction\": ").append(compParameter.getBeanProperty("showDetailAction"))
				.append(",");
		sb.append("\"effects\": Browser.effects && ")
				.append(compParameter.getBeanProperty("effects"));
		sb.append("});");

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("__progressbar_actions_init(").append(actionFunc).append(");");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString(), sb2.toString());
	}
}
