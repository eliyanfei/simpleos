package net.simpleframework.content.component.vote;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VoteItem extends AbstractIdDataObjectBean {
	private static final long serialVersionUID = 1298079870087845908L;

	private String text;

	private ID groupId;

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public ID getGroupId() {
		return groupId;
	}

	public void setGroupId(final ID groupId) {
		this.groupId = groupId;
	}
}
