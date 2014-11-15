package net.simpleframework.my.message;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ExcludeUserBean extends AbstractIdDataObjectBean {
	private EMessageType messageType;

	// messageType == EMessageType.broadcast, typeId==messageId
	// messageType == EMessageType.user, typeId==userId
	// messageType == EMessageType.notification, typeId==ENotification
	private Object typeId;

	private ID userId;

	public EMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(final EMessageType messageType) {
		this.messageType = messageType;
	}

	public Object getTypeId() {
		return typeId;
	}

	public void setTypeId(final Object typeId) {
		this.typeId = typeId;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	private static final long serialVersionUID = 7901748136233368319L;
}
