package net.simpleframework.applets.notification.component.window;

import java.util.Collection;

import net.simpleframework.applets.notification.IMessageNotification;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.window.AbstractWindowHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultMessageWindowHandle extends AbstractWindowHandle implements
		IMessageWindowHandle {

	@Override
	public Collection<? extends IMessageNotification> getMessageNotifications(
			final ComponentParameter compParameter) {
		return null;
	}
}
