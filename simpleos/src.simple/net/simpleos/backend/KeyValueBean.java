package net.simpleos.backend;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 自定义字段项
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:13:36
 */
public class KeyValueBean extends AbstractIdDataObjectBean {
	private String name;
	private String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
