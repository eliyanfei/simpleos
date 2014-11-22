package net.simpleos.backend.links;

import java.util.Map;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleos.SimpleosUtil;
import net.simpleos.utils.LangUtils;

/**
 * 
 * @Description：
 * @author: 李岩飞
 * @Time: Apr 1, 2011 8:45:32 PM
 */
public class LinksAction extends AbstractAjaxRequestHandle {

	/**
	 * 保存
	 */
	public IForward linksSave(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				if (SimpleosUtil.isManage(compParameter)) {
					String linksId = compParameter.getParameter("linksId");
					LinksBean linksBean = LinksUtils.appModule.getBean(LinksBean.class, linksId);
					if (linksBean == null) {
						linksBean = new LinksBean();
						linksBean.setUserId(SimpleosUtil.getLoginAccount(compParameter).getId());
						linksBean.setOorder(LangUtils.toInt(LinksUtils.appModule.getDataObjectManager(LinksBean.class).nextId("id").getValue(),
								0));
					}
					linksBean.setTitle(compParameter.getParameter("l_title"));
					linksBean.setDescription(compParameter.getParameter("l_description"));
					linksBean.setColor(compParameter.getParameter("l_color"));
					linksBean.setUrl(compParameter.getParameter("l_url"));
					LinksUtils.appModule.doUpdate(linksBean);
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
				if (SimpleosUtil.isManage(compParameter)) {
					final String linksId = compParameter.getParameter("linksId");
					final LinksBean linksBean = LinksUtils.appModule.getBean(LinksBean.class, linksId);
					if (linksBean != null) {
						LinksUtils.appModule.doDelete(linksBean);
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
				LinksBean linksBean1 = LinksUtils.appModule.getBean(LinksBean.class, linksId1);
				LinksBean linksBean2 = LinksUtils.appModule.getBean(LinksBean.class, linksId2);
				if (linksBean1 == null || linksBean2 == null) {
					return;
				}
				int oorder = linksBean1.getOorder();
				linksBean1.setOorder(linksBean2.getOorder());
				linksBean2.setOorder(oorder);
				LinksUtils.appModule.doUpdate(linksBean1);
				LinksUtils.appModule.doUpdate(linksBean2);
			}
		});
	}
}
