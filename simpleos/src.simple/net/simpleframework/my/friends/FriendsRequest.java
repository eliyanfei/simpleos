package net.simpleframework.my.friends;

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
public class FriendsRequest extends AbstractIdDataObjectBean {
	private ID sentId;

	private Date sentDate;

	private String messageText;

	private ID toId;

	private ERequestStatus requestStatus;

	private ID groupId;

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

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(final String messageText) {
		this.messageText = messageText;
	}

	public ID getToId() {
		return toId;
	}

	public void setToId(final ID toId) {
		this.toId = toId;
	}

	public ERequestStatus getRequestStatus() {
		if (requestStatus == null) {
			requestStatus = ERequestStatus.request;
		}
		return requestStatus;
	}

	public void setRequestStatus(final ERequestStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	public ID getGroupId() {
		return groupId;
	}

	public void setGroupId(final ID groupId) {
		this.groupId = groupId;
	}

	private static final long serialVersionUID = 7670183558206576860L;
}
