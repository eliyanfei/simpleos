package net.prj.manager;

import java.util.Map;

import net.itniwo.commons.LangUtils;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:26:11
 */
public class PrjMgrAction extends AbstractAjaxRequestHandle {
	/**
	 * 保存自定义变量
	 * @param compParameter
	 * @return
	 */
	public IForward saveTemplate(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String name = "template";
				final StringBuffer sb = new StringBuffer();
				sb.append("name=").append(compParameter.getParameter("templateId"));
				sb.append(";fixedHeader=").append(compParameter.getParameter("fixedHeader"));
				sb.append(";fixedFooter=").append(compParameter.getParameter("fixedFooter"));
				sb.append(";fullScreen=").append(compParameter.getParameter("fullScreen"));
				PrjCustomBean customBean = PrjMgrUtils.appModule.getBeanByExp(PrjCustomBean.class, "name=? and type=?", new Object[] { name, name });
				if (customBean == null) {
					customBean = new PrjCustomBean();
					customBean.setType(name);
					customBean.setName(name);
				}
				customBean.setValue(sb.toString());
				PrjMgrUtils.appModule.doUpdate(customBean);
			}
		});
	}

	/**
	 * 保存幻灯片
	 * @param compParameter
	 * @return
	 */
	public IForward saveNav(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String navId = compParameter.getParameter("navId");
				final String title = compParameter.getParameter("ds_title");
				final String url = compParameter.getParameter("ds_url");
				PrjNavBean navBean = PrjMgrUtils.appModule.getBean(PrjNavBean.class, navId);
				if (navBean == null) {
					navBean = new PrjNavBean();
					navBean.setOorder(PrjMgrUtils.appModule.nextId());
				}
				navBean.setImage("");
				navBean.setTitle(title);
				navBean.setUrl(url);
				PrjMgrUtils.appModule.doUpdate(navBean);
			}
		});
	}

	/**
	 * 删除幻灯片
	 * @param compParameter
	 * @return
	 */
	public IForward deleteNav(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String navId = compParameter.getParameter("navId");
				PrjMgrUtils.appModule.doDelete(PrjNavBean.class, navId);
			}
		});
	}

	/**
	 * 保存幻灯片
	 * @param compParameter
	 * @return
	 */
	public IForward exchangeNav(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String navId1 = compParameter.getParameter("navId1");
				final String navId2 = compParameter.getParameter("navId2");
				final PrjNavBean navBean1 =PrjMgrUtils.appModule.getBean(PrjNavBean.class, navId1);
				final PrjNavBean navBean2 =PrjMgrUtils.appModule.getBean(PrjNavBean.class, navId2);
				final boolean up = LangUtils.toBoolean(compParameter.getParameter("up"), false);
				PrjMgrUtils.appModule.getDataObjectManager(PrjNavBean.class).exchange(navBean1, navBean2, new Column("oorder"), up);
			}
		});
	}
}
