package net.itsite.user;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 名人名言
 * @author 李岩飞
 *
 */
public class WisdomBean extends AbstractIdDataObjectBean {
	private ID id;
	private String content;
	private int type = 0;//类型,待定义

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
