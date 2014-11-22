package net.simpleos.impl;

import net.simpleframework.util.ConvertUtils;

import org.dom4j.Element;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:44:48
 */
public class PrjColumn {
	private String name;
	private String text;
	private String defValue;
	private boolean mul;

	public PrjColumn(String name, Element ele) {
		super();
		this.name = name + "_" + ele.attributeValue("name");
		this.text = ele.attributeValue("text");
		this.mul = ConvertUtils.toBoolean(ele.attributeValue("mul"), false);
		this.defValue = ele.attributeValue("default");
	}

	public boolean isMul() {
		return mul;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	public String getDefValue() {
		return defValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
