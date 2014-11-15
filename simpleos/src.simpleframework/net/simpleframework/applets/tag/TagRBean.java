package net.simpleframework.applets.tag;

import net.simpleframework.core.bean.AbstractDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TagRBean extends AbstractDataObjectBean {
	private ID tagId;

	private ID rid;

	public ID getTagId() {
		return tagId;
	}

	public void setTagId(final ID tagId) {
		this.tagId = tagId;
	}

	public ID getRid() {
		return rid;
	}

	public void setRid(final ID rid) {
		this.rid = rid;
	}

	private static final long serialVersionUID = -4698059815398303527L;
}
