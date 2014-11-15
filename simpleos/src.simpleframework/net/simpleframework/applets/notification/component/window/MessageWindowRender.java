package net.simpleframework.applets.notification.component.window;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.window.WindowRender;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageWindowRender extends WindowRender {

	public MessageWindowRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final MessageWindowBean messageWindowBean = (MessageWindowBean) compParameter.componentBean;
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getJavascriptCode(compParameter));
		sb.append("__message_window_actions_init(").append(messageWindowBean.getActionFunction()).append(", '")
				.append(compParameter.getBeanProperty("name")).append("', '").append("ajaxRequest_").append(messageWindowBean.hashId()).append("', ")
				.append(compParameter.getBeanProperty("frequency")).append(", ").append(compParameter.getBeanProperty("closeDelay"))
				.append(",true);");
		return sb.toString();
	}
}
