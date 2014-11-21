package net.simpleframework.my.message;

import java.util.Date;

import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SimpleMessage extends AbstractIdDataObjectBean {
	private EMessageType messageType;

	private ENotification notification;

	private ID sentId;

	private Date sentDate;

	private String subject;

	private String textBody;

	// messageType != EMessageType.broadcast
	private ID toId;

	// messageType == EMessageType.broadcast
	private String jobView;

	private String messageUrl;

	private Date expiredDate;

	// 广播的消息是否阅读，暂写到cookie中
	private boolean messageRead;

	private EContentStatus status;

	public EMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(final EMessageType messageType) {
		this.messageType = messageType;
	}

	public ENotification getNotification() {
		return notification;
	}

	public void setNotification(final ENotification notification) {
		this.notification = notification;
	}

	public ID getSentId() {
		return sentId;
	}

	public void setSentId(final ID sentId) {
		this.sentId = sentId;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(final Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getTextBody() {
		return textBody;
	}

	public void setTextBody(final String textBody) {
		this.textBody = textBody;
	}

	public ID getToId() {
		return toId;
	}

	public void setToId(final ID toId) {
		this.toId = toId;
	}

	public String getJobView() {
		return jobView;
	}

	public void setJobView(final String jobView) {
		this.jobView = jobView;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(final Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getMessageUrl() {
		return messageUrl;
	}

	public void setMessageUrl(final String messageUrl) {
		this.messageUrl = messageUrl;
	}

	public boolean isMessageRead() {
		return messageRead;
	}

	public void setMessageRead(final boolean messageRead) {
		this.messageRead = messageRead;
	}

	public EContentStatus getStatus() {
		return status;
	}

	public void setStatus(final EContentStatus status) {
		this.status = status;
	}

	private static final long serialVersionUID = 2682993790158744475L;
}
