package net.itsite.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:44:48
 */
public class PrjColumns {
	private Map<String, PrjColumn> columnMap = new LinkedHashMap<String, PrjColumn>();
	private String name;
	private String ids;

	@SuppressWarnings("unchecked")
	public PrjColumns(Element ele) {
		this.name = ele.attributeValue("name");
		this.ids = ele.attributeValue("ids");
		final List<Element> list = ele.elements();
		for (Element subEle : list) {
			PrjColumn column = new PrjColumn(this.name, subEle);
			columnMap.put(column.getName(), column);
		}
	}

	public Map<String, PrjColumn> getColumnMap() {
		return columnMap;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getIds() {
		return ids;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
