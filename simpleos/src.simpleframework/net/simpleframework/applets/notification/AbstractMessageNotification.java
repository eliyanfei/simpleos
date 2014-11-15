package net.simpleframework.applets.notification;

import java.util.Date;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractMessageNotification implements IMessageNotification {

	private String subject;

	private String textBody;

	private Date sentDate;

	@Override
	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@Override
	public String getTextBody() {
		return textBody;
	}

	public void setTextBody(final String textBody) {
		this.textBody = textBody;
	}

	@Override
	public Date getSentDate() {
		return sentDate == null ? new Date() : sentDate;
	}

	public void setSentDate(final Date sentDate) {
		this.sentDate = sentDate;
	}
}
