package net.simpleos.module.docu;

import net.simpleframework.web.page.component.ui.portal.module.AbstractPortalModuleHandle;
import net.simpleos.module.ASimpleosModule;

/**  
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 下午2:13:05 
 *	 
*/
public class SimpleosDocuModule extends ASimpleosModule {

	@Override
	public String getBackendJsp() {
		return super.getBackendJsp();
	}
	
	@Override
	public String getFrontHtml() {
		return "/docu.html";
	}

	@Override
	public String getFrontTitle() {
		return "资料";
	}

	@Override
	public String getModuleName() {
		return "docu";
	}

	@Override
	public int getOorder() {
		return 400;
	}

	@Override
	public AbstractPortalModuleHandle getPortalModuleHandle() {
		return new DocuPortalModule(null);
	}
}
