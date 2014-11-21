package net.simpleframework.my.space;

import java.util.Date;

import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.web.EFunctionModule;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SapceLogBean extends AbstractIdDataObjectBean implements IContentBeanAware {
	private String content;

	private Date createDate;

	private ID userId;

	private EFunctionModule refModule;

	private ID refId;

	private ID replyFrom;

	private int remarks;

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public EFunctionModule getRefModule() {
		return refModule == null ? EFunctionModule.space_log : refModule;
	}

	public void setRefModule(final EFunctionModule refModule) {
		this.refModule = refModule;
	}

	public ID getRefId() {
		return refId;
	}

	public void setRefId(final ID refId) {
		this.refId = refId;
	}

	public ID getReplyFrom() {
		return replyFrom;
	}

	public void setReplyFrom(final ID replyFrom) {
		this.replyFrom = replyFrom;
	}

	public int getRemarks() {
		return remarks;
	}

	public void setRemarks(final int remarks) {
		this.remarks = remarks;
	}

	private static final long serialVersionUID = -1736201811093938606L;
}
