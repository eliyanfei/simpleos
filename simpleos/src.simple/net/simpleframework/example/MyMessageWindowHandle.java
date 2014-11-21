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
		smn.setSubject("您有3项未读工作");
		smn.setTextBody("Welcome to the home of Spring, "
				+ "the leading platform to build and run enterprise Java applications. "
				+ "Led and sustained by SpringSource, "
				+ "Spring delivers significant benefits for many projects, "
				+ "increasing development productivity and runtime performance "
				+ "while improving test coverage and application quality.");
		coll.add(smn);
		smn = new SystemMessageNotification();
		smn.setSubject("您接收到3个最新邮件，请注意查收您的油箱！");
		smn.setTextBody("北京时间1月22日消息，国家体育总局今天上午正式宣布：原水上运动管理中"
				+ "心主任韦迪接替南勇，担任足球管理中心主任兼党委书记，同时免去南勇足管中心党委书记及主任、免去杨一民足管中心副主任职务。");
		coll.add(smn);
		return coll;
	}

}
