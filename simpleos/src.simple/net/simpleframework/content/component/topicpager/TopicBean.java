package net.simpleframework.content.component.topicpager;

import java.util.Date;

import net.simpleframework.content.AbstractContent;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.IAttentionsBeanAware;
import net.simpleframework.core.bean.IViewsBeanAware;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TopicBean extends AbstractContent implements IViewsBeanAware, IAttentionsBeanAware {
	public static final short MARK_ATTACH = 1;

	public static final short MARK_VOTE = 2;

	public static final short MARK_IMAGE = 4;

	private ID catalogId;

	private String topic, keywords;

	private ID lastpostId;

	private Date lastPostUpdate;

	private long views;

	private int replies;

	private int attentions; // 关注

	private short star;

	private EContentType ttype;

	public ID getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(final ID catalogId) {
		this.catalogId = catalogId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(final String keywords) {
		this.keywords = keywords;
	}

	public ID getLastpostId() {
		return lastpostId;
	}

	public void setLastpostId(final ID lastpostId) {
		this.lastpostId = lastpostId;
	}

	@Override
	public long getViews() {
		return views;
	}

	@Override
	public void setViews(final long views) {
		this.views = views;
	}

	public int getReplies() {
		return replies;
	}

	public void setReplies(final int replies) {
		this.replies = replies;
	}

	@Override
	public int getAttentions() {
		return attentions;
	}

	@Override
	public void setAttentions(final int attentions) {
		this.attentions = attentions;
	}

	public short getStar() {
		return star;
	}

	public void setStar(final short star) {
		this.star = star;
	}

	public Date getLastPostUpdate() {
		return lastPostUpdate != null ? lastPostUpdate : getLastUpdate();
	}

	public void setLastPostUpdate(final Date lastPostUpdate) {
		this.lastPostUpdate = lastPostUpdate;
	}

	public EContentType getTtype() {
		return ttype == null ? EContentType.normal : ttype;
	}

	public void setTtype(final EContentType ttype) {
		this.ttype = ttype;
	}

	@Override
	public String toString() {
		return getTopic();
	}

	private static final long serialVersionUID = 8506599721649534118L;
}
