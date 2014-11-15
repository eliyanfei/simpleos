package net.prj.manager.site;

import java.util.Map;

import net.a.ItSiteUtil;
import net.itniwo.commons.StringsUtils;
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
public class PrjSiteAction extends AbstractAjaxRequestHandle {
	/**
	 * 系统设置
	 * @param compParameter
	 * @return
	 */
	public IForward saveSys(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				PrjMgrUtils.saveCustom("sys", (AItSiteAppclicationModule) PrjSiteUtils.appModule, compParameter);
				ItSiteUtil.url = compParameter.getParameter("site_url");
				ItSiteUtil.title = compParameter.getParameter("site_title");
			}
		});
	}

	/**
	 * 保存站点信息
	 * @param compParameter
	 * @return
	 */
	public IForward saveSite(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				PrjMgrUtils.saveCustom("site", (AItSiteAppclicationModule) PrjSiteUtils.appModule, compParameter);
			}
		});
	}

	/**
	 * 保存友链信息
	 * @param compParameter
	 * @return
	 */
	public IForward saveLinks(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				json.put("rs", StringsUtils.trimNull(compParameter.getParameter("rs"), "true"));
				PrjMgrUtils.saveCustom("links", (AItSiteAppclicationModule) PrjSiteUtils.appModule, compParameter);
			}
		});
	}

	/**
	 * 保存开发登入信息
	 * @param compParameter
	 * @return
	 */
	public IForward saveOpen(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				PrjMgrUtils.saveCustom("open", (AItSiteAppclicationModule) PrjSiteUtils.appModule, compParameter);
			}
		});
	}
}