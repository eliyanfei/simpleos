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
public class SystemMessageNotification extends AbstractIdDataObjectBean implements
		IMessageNotification {
	private ID sentId;

	private Date sentDate;

	private ID toId;

	private ID messageId;

	private String subject;

	private String textBody;

	public ID getSentId() {
		return sentId;
	}

	public void setSentId(final ID sentId) {
		this.sentId = sentId;
	}

	public ID getToId() {
		return toId;
	}

	public void setToId(final ID toId) {
		this.toId = toId;
	}

	@Override
	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(final Date sentDate) {
		this.sentDate = sentDate;
	}

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

	public ID getMessageId() {
		return messageId;
	}

	public void setMessageId(final ID messageId) {
		this.messageId = messageId;
	}

	private static final long serialVersionUID = 6329263825074181432L;
}
