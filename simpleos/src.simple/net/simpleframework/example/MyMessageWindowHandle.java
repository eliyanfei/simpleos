package net.simpleframework.example;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.applets.notification.IMessageNotification;
import net.simpleframework.applets.notification.SystemMessageNotification;
import net.simpleframework.applets.notification.component.window.DefaultMessageWindowHandle;
import net.simpleframework.web.page.component.ComponentParameter;

public class MyMessageWindowHandle extends DefaultMessageWindowHandle {
	@Override
	public Collection<IMessageNotification> getMessageNotifications(
			final ComponentParameter compParameter) {
		final Collection<IMessageNotification> coll = new ArrayList<IMessageNotification>();
		SystemMessageNotification smn = new SystemMessageNotification();
		smn.setSubject("您有1项未读工作");
		smn.setTextBody("helle world.");
		coll.add(smn);
		return coll;
	}

}
