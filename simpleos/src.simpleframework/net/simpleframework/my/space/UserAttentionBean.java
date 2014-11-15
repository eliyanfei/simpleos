package net.simpleframework.my.space;

import java.util.Date;

import net.simpleframework.core.bean.AbstractDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserAttentionBean extends AbstractDataObjectBean {

	private ID userId;

	private ID attentionId;

	private Date createDate;

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public ID getUserId() {
		return userId;
	}

	public void setAttentionId(final ID attentionId) {
		this.attentionId = attentionId;
	}

	public ID getAttentionId() {
		return attentionId;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	private static final long serialVersionUID = 3954920675820984288L;
}
