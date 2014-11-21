package net.simpleframework.applets.notification;

import java.util.List;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public final class MailSenderList {
	private List<MailSender> senderList;

	public List<MailSender> getSenderList() {
		return senderList;
	}

	public void setSenderList(final List<MailSender> senderList) {
		this.senderList = senderList;
	}
}
