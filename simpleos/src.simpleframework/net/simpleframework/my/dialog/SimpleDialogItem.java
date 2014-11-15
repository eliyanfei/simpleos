package net.simpleframework.my.dialog;

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
public class SimpleDialogItem extends AbstractIdDataObjectBean {

	private ID dialogId;
	private Date sentDate;
	private boolean me;
	private String content;

	public SimpleDialogItem() {
		this.sentDate = new Date();
	}

	public void setDialogId(ID dialogId) {
		this.dialogId = dialogId;
	}

	public void setMe(boolean me) {
		this.me = me;
	}

	public boolean isMe() {
		return me;
	}

	public ID getDialogId() {
		return dialogId;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
