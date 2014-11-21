package net.simpleframework.applets.notification;

import java.util.Date;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NotificationLogBean extends AbstractIdDataObjectBean {
	private ID toId;

	private ENotificationEvent notificationEvent;

	private Date sentDate;

	public ID getToId() {
		return toId;
	}

	public void setToId(final ID toId) {
		this.toId = toId;
	}

	public ENotificationEvent getNotificationEvent() {
		return notificationEvent;
	}

	public void setNotificationEvent(final ENotificationEvent notificationEvent) {
		this.notificationEvent = notificationEvent;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(final Date sentDate) {
		this.sentDate = sentDate;
	}

	private static final long serialVersionUID = 6498964031214611672L;
}
