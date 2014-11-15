package net.simpleframework.content.component.newspager;

import java.util.Date;

import net.simpleframework.content.AbstractContent;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.IAttentionsBeanAware;
import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.IViewsBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.HTMLUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsBean extends AbstractContent implements IViewsBeanAware, IAttentionsBeanAware,
		IDescriptionBeanAware, IContentBeanAware {
	static final String expiredDateFormat = "yyyy-MM-dd";

	private ID catalogId;

	private ENewsTemplate viewTemplate;

	private String topic, topic2, keywords, author, source, content, description;

	private Date expiredDate;

	private long views;

	private int remarks;

	private int attentions; // 关注

	private boolean allowComments = true;

	private EContentType ttype;

	public ID getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(final ID catalogId) {
		this.catalogId = catalogId;
	}

	public ENewsTemplate getViewTemplate() {
		return viewTemplate != null ? viewTemplate : ENewsTemplate.t_news;
	}

	public void setViewTemplate(final ENewsTemplate viewTemplate) {
		this.viewTemplate = viewTemplate;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public String getTopic2() {
		return topic2;
	}

	public void setTopic2(final String topic2) {
		this.topic2 = topic2;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(final String keywords) {
		this.keywords = keywords;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = HTMLUtils.stripScripts(content);
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(final Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public long getViews() {
		return views;
	}

	@Override
	public void setViews(final long views) {
		this.views = views;
	}

	public int getRemarks() {
		return remarks;
	}

	public void setRemarks(final int remarks) {
		this.remarks = remarks;
	}

	@Override
	public int getAttentions() {
		return attentions;
	}

	@Override
	public void setAttentions(final int attentions) {
		this.attentions = attentions;
	}

	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(final boolean allowComments) {
		this.allowComments = allowComments;
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

	private static final long serialVersionUID = 60590822032665502L;
}
