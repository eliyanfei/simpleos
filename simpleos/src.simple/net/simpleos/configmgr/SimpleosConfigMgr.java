package net.simpleos.configmgr;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.itsite.utils.Dom4jUtils;

import org.dom4j.Document;
import org.dom4j.Element;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 上午9:42:49 
 * @Description: 存储一些常量信息
 *	 
*/
public class SimpleosConfigMgr {
	private static SimpleosConfigMgr configMgr = null;

	private SimpleosConfigMgr() {
	}

	public void init(String path) {
		try {
			final Document document = Dom4jUtils.createDocument(new File(path + File.separator + "config-list.xml"));
			final List<Element> eleList = document.getRootElement().elements();
			for (final Element ele : eleList) {
				final String refPath = path + File.separator + ele.attributeValue("value");
				loadRefConfig(refPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final Map<String, List<String>> configMap = new HashMap<String, List<String>>();

	public List<String> getConfigList(String name) {
		List<String> list = configMap.get(name);
		return list != null ? list : new ArrayList<String>();
	}

	/**
	 * 加载所有的引用配置文件
	 */
	private void loadRefConfig(String refPath) {
		try {
			final Document document = Dom4jUtils.createDocument(new File(refPath));
			final Element rootEle = document.getRootElement();
			final List<Element> eleList = rootEle.elements();
			final List<String> list = new ArrayList<String>();
			for (final Element ele : eleList) {
				list.add(ele.attributeValue("value"));
			}
			configMap.put(rootEle.getName(), list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SimpleosConfigMgr getConfigMgr() {
		if (configMgr == null)
			configMgr = new SimpleosConfigMgr();
		return configMgr;
	}
}
