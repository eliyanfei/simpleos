package net.simpleframework.my.dialog;

import java.util.Date;

import net.itsite.ItSiteUtil;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.util.IConstants;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SimpleDialog extends AbstractIdDataObjectBean {

	private ID sentId;
	private boolean sentDel = false;
	private boolean sendRead = true;
	private ID toId;
	private boolean toDel = false;
	private boolean toRead = true;

	private Date lastDate;
	private Date createDate;

	public SimpleDialog() {
		this.lastDate = new Date();
		this.createDate = new Date();
	}

	public boolean isSendRead() {
		return sendRead;
	}

	public boolean isToRead() {
		return toRead;
	}

	public void setSendRead(boolean sendRead) {
		this.sendRead = sendRead;
	}

	public void setToRead(boolean toRead) {
		this.toRead = toRead;
	}

	public boolean isSentDel() {
		return sentDel;
	}

	public void setSentDel(boolean sentDel) {
		this.sentDel = sentDel;
	}

	public boolean isToDel() {
		return toDel;
	}

	public void setToDel(boolean toDel) {
		this.toDel = toDel;
	}

	public ID getSentId() {
		return sentId;
	}

	public void setSentId(ID sentId) {
		this.sentId = sentId;
	}

	public ID getToId() {
		return toId;
	}

	public void setToId(ID toId) {
		this.toId = toId;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSentUserText() {
		final IUser user = ItSiteUtil.getUserById(getSentId());
		return user != null ? user.getText() : IConstants.HTML_BLANK_STRING;
	}

	public String getToUserText() {
		final IUser user = ItSiteUtil.getUserById(getToId());
		return user != null ? user.getText() : IConstants.HTML_BLANK_STRING;
	}

	public boolean canDel() {
		return this.sentDel && this.toDel;
	}

	private static final long serialVersionUID = 2682993790158744475L;
}
