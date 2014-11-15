package net.prj.manager.links;

import java.util.Map;

import net.a.ItSiteUtil;
import net.itniwo.commons.LangUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 
 * @Description：
 * @author: 李岩飞
 * @Time: Apr 1, 2011 8:45:32 PM
 */
public class PrjLinksAction extends AbstractAjaxRequestHandle {

	/**
	 * 保存
	 */
	public IForward linksSave(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				if (ItSiteUtil.isManage(compParameter)) {
					String linksId = compParameter.getParameter("linksId");
					PrjLinksBean linksBean = PrjLinksUtils.appModule.getBean(PrjLinksBean.class, linksId);
					if (linksBean == null) {
						linksBean = new PrjLinksBean();
						linksBean.setUserId(ItSiteUtil.getLoginAccount(compParameter).getId());
						linksBean.setOorder(LangUtils.toInt(PrjLinksUtils.appModule.getDataObjectManager(PrjLinksBean.class).nextId("id").getValue(),
								0));
					}
					linksBean.setTitle(compParameter.getParameter("l_title"));
					linksBean.setDescription(compParameter.getParameter("l_description"));
					linksBean.setColor(compParameter.getParameter("l_color"));
					linksBean.setUrl(compParameter.getParameter("l_url"));
					PrjLinksUtils.appModule.doUpdate(linksBean);
					json.put("act", "true");
				} else {
					json.put("act", "fail");
				}
			}
		});
	}

	/**
	 * 删除
	 */
	public IForward linksDelete(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				if (ItSiteUtil.isManage(compParameter)) {
					final String linksId = compParameter.getParameter("linksId");
					final PrjLinksBean linksBean = PrjLinksUtils.appModule.getBean(PrjLinksBean.class, linksId);
					if (linksBean != null) {
						PrjLinksUtils.appModule.doDelete(linksBean);
					}
				}
			}
		});
	}

	public IForward exchangeLinks(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String linksId1 = compParameter.getRequestParameter("linksId1");
				final String linksId2 = compParameter.getRequestParameter("linksId2");
				PrjLinksBean linksBean1 = PrjLinksUtils.appModule.getBean(PrjLinksBean.class, linksId1);
				PrjLinksBean linksBean2 = PrjLinksUtils.appModule.getBean(PrjLinksBean.class, linksId2);
				if (linksBean1 == null || linksBean2 == null) {
					return;
				}
				int oorder = linksBean1.getOorder();
				linksBean1.setOorder(linksBean2.getOorder());
				linksBean2.setOorder(oorder);
				PrjLinksUtils.appModule.doUpdate(linksBean1);
				PrjLinksUtils.appModule.doUpdate(linksBean2);
			}
		});
	}
}
