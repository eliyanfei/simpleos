package net.simpleos.module;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.simpleframework.web.page.component.ui.portal.module.AbstractPortalModuleHandle;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;
import net.simpleos.utils.Dom4jUtils;

import org.dom4j.Document;
import org.dom4j.Element;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午2:04:24 
 *	 
*/
public abstract class ASimpleosModule implements ISimpleosModule {
	public ASimpleosModule() {
		list = new ArrayList<SimpleosModuleBean>();
		parserModuleBean();
		final AbstractPortalModuleHandle portalModuleHandle = getPortalModuleHandle();
		if (portalModuleHandle != null) {
			PortalModuleRegistryFactory.regist(portalModuleHandle.getClass(), portalModuleHandle.getName(), portalModuleHandle.getText(),
					portalModuleHandle.getCatalog(), portalModuleHandle.getIcon(), portalModuleHandle.getDescription());
		}
	}

	protected List<SimpleosModuleBean> list;

	/**
	 * 解析配置文件
	 */
	protected void parserModuleBean() {
		try {
			final InputStream is = this.getClass().getResourceAsStream("module.xml");
			if (is == null)
				return;
			final Document docu = Dom4jUtils.createDocument(is);
			final List<Element> eleList = docu.getRootElement().elements();
			for (final Element ele : eleList) {
				list.add(new SimpleosModuleBean(ele));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(ISimpleosModule o) {
		if (o.getOorder() > getOorder()) {
			return -1;
		} else if (o.getOorder() < getOorder()) {
			return 1;
		}
		return 0;
	}

	@Override
	public String getFrontHtml() {
		return null;
	}

	@Override
	public List<SimpleosModuleBean> getBackendActions() {
		return list;
	}

	@Override
	public AbstractPortalModuleHandle getPortalModuleHandle() {
		return null;
	}

	@Override
	public String getBackendJsp() {
		return null;
	}
}
