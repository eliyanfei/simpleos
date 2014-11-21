package net.simpleframework.content.bbs;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BbsForumStat extends AbstractIdDataObjectBean {
	private static final long serialVersionUID = 398674439302010539L;

	private int topics;

	private int messages;

	private ID lastpostId;

	public int getTopics() {
		return topics;
	}

	public void setTopics(final int topics) {
		this.topics = topics;
	}

	public int getMessages() {
		return messages;
	}

	public void setMessages(final int messages) {
		this.messages = messages;
	}

	public ID getLastpostId() {
		return lastpostId;
	}

	public void setLastpostId(final ID lastpostId) {
		this.lastpostId = lastpostId;
	}
}
