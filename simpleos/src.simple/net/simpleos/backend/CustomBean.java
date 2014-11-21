package net.simpleos.backend;

import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 自定义字段项
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:13:36
 */
public class CustomBean extends AbstractIdDataObjectBean {
	private String name;
	private String type;
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
