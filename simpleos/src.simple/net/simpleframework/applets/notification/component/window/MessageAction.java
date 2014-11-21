package net.simpleframework.applets.notification.component.window;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.applets.notification.IMessageNotification;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.JsonForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MessageAction extends AbstractAjaxRequestHandle {

	@Override
	public IForward ajaxProcess(final ComponentParameter compParameter) {
		final Map<String, Object> json = new HashMap<String, Object>();
		final ComponentParameter nComponentParameter = MessageWindowUtils.getComponentParameter(compParameter);
		final IMessageWindowHandle messageWindowHandle = (IMessageWindowHandle) nComponentParameter.getComponentHandle();
		if (DefaultMessageWindowHandle.class.equals(messageWindowHandle.getClass())) {
			json.put("showMessageNotification", true);
		} else {
			final Collection<? extends IMessageNotification> coll = messageWindowHandle.getMessageNotifications(nComponentParameter);
			json.put("showMessageNotification", coll != null && coll.iterator().hasNext());
		}
		return new JsonForward(json);
	}
}