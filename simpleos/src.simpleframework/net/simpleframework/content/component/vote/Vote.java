package net.simpleframework.content.component.vote;

import java.util.Date;

import net.simpleframework.content.AbstractContentBase;
import net.simpleframework.core.bean.IDescriptionBeanAware;
import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Vote extends AbstractContentBase implements ITextBeanAware, IDescriptionBeanAware {
	// public static final short STATUS_NORMAL = 0;
	//
	// public static final short STATUS_EXPIRED = 1;
	//
	// public static final short STATUS_CLOSE = 2;

	private static final long serialVersionUID = -1208914879108337089L;

	static final String expiredDateFormat = "yyyy-MM-dd";

	private ID documentId;

	private String text;

	private String description;

	private Date expiredDate;

	public ID getDocumentId() {
		return documentId;
	}

	public void setDocumentId(final ID documentId) {
		this.documentId = documentId;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(final String text) {
		this.text = text;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(final Date expiredDate) {
		this.expiredDate = expiredDate;
	}
}
