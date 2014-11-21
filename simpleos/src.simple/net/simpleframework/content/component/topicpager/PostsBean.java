package net.simpleframework.content.component.topicpager;

import net.simpleframework.content.AbstractContentBase;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PostsBean extends AbstractContentBase {
	private ID topicId;

	private boolean firstPost;

	private String ip;

	private ID quoteId;

	public ID getTopicId() {
		return topicId;
	}

	public void setTopicId(final ID topicId) {
		this.topicId = topicId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	public boolean isFirstPost() {
		return firstPost;
	}

	public void setFirstPost(final boolean firstPost) {
		this.firstPost = firstPost;
	}

	public ID getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(final ID quoteId) {
		this.quoteId = quoteId;
	}

	private static final long serialVersionUID = 3608344223619377401L;
}
