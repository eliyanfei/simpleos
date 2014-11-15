package net.prj.manager.company;

import java.util.Map;

import net.itsite.impl.AItSiteAppclicationModule;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:26:11
 */
public class PrjCompanyAction extends AbstractAjaxRequestHandle {
	/**
	 * 系统设置
	 * @param compParameter
	 * @return
	 */
	public IForward saveContact(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				PrjMgrUtils.saveCustom("contact", (AItSiteAppclicationModule) PrjCompanyUtils.appModule, compParameter);
			}
		});
	}

	/**
	 * 保存站点信息
	 * @param compParameter
	 * @return
	 */
	public IForward saveCompany(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				PrjMgrUtils.saveCustom("company", (AItSiteAppclicationModule) PrjCompanyUtils.appModule, compParameter);
			}
		});
	}

}
