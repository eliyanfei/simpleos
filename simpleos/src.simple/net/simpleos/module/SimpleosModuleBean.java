package net.simpleos.module;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午2:00:33 
 *	 
*/
public class SimpleosModuleBean {
	public String name;//唯一值
	public String text;//显示名称
	public String action;//onclick函数
	public List<SimpleosModuleBean> moduleList = new ArrayList<SimpleosModuleBean>();

	public SimpleosModuleBean(final Element moduleEle) {
		try {
			text = moduleEle.attributeValue("text");
			name = moduleEle.attributeValue("name");
			action = moduleEle.elementText("action");
			final List<Element> eleList = moduleEle.elements();
			if (eleList != null)
				for (final Element ele : eleList) {
					moduleList.add(new SimpleosModuleBean(ele));
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
