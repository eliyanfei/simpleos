package net.simpleframework.content.component.remark;

import net.simpleframework.content.AbstractContent;
import net.simpleframework.content.IContentBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class RemarkItem extends AbstractContent implements ITreeBeanAware, IContentBeanAware {
	private static final long serialVersionUID = -8721255256891715140L;

	private ID parentId;

	private ID documentId;

	private String ip;

	private String content;

	private int support, opposition;

	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	@Override
	public ID getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(final ID parentId) {
		this.parentId = parentId;
	}

	public ID getDocumentId() {
		return documentId;
	}

	public void setDocumentId(final ID documentId) {
		this.documentId = documentId;
	}

	public int getSupport() {
		return support;
	}

	public void setSupport(final int support) {
		this.support = support;
	}

	public int getOpposition() {
		return opposition;
	}

	public void setOpposition(final int opposition) {
		this.opposition = opposition;
	}

	@Override
	public RemarkItem parent() {
		return null;
	}
}