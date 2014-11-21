package net.simpleframework.web.page.component.ui.calendar;

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
public class CalendarRender extends AbstractComponentJavascriptRender {
	public CalendarRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = compParameter.componentBean.getActionFunction();
		sb.append(actionFunc).append(".calendar = new CalendarDateSelect({");

		final String dateFormat = (String) compParameter.getBeanProperty("dateFormat");
		if (StringUtils.hasText(dateFormat)) {
			sb.append("dateFormat: \"").append(dateFormat).append("\",");
		}
		final String closeCallback = (String) compParameter.getBeanProperty("closeCallback");
		if (StringUtils.hasText(closeCallback)) {
			sb.append("onclose: function(cal) {");
			sb.append(closeCallback);
			sb.append("},");
		}
		sb.append("showTime: ").append(compParameter.getBeanProperty("showTime")).append(",");
		sb.append("effects: Browser.effects && ").append(compParameter.getBeanProperty("effects"));
		sb.append("});");

		// show函数
		sb.append(actionFunc).append(".show = function(inputField, dateFormat) { ");
		sb.append(actionFunc).append(".calendar.show(inputField || \"")
				.append(compParameter.getBeanProperty("inputField")).append("\", dateFormat); };");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
