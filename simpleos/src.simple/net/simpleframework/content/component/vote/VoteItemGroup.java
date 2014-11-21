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
public class VoteItemGroup extends AbstractIdDataObjectBean {
	private static final long serialVersionUID = -3536860674623739975L;

	private ID voteId;

	private String text;

	private short multiple;

	public ID getVoteId() {
		return voteId;
	}

	public void setVoteId(final ID voteId) {
		this.voteId = voteId;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public short getMultiple() {
		return multiple;
	}

	public void setMultiple(final short multiple) {
		this.multiple = multiple;
	}
}
