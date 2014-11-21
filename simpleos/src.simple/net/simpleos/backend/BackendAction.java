package net.simpleos.backend;

import java.util.Map;

import net.itsite.utils.LangUtils;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleos.backend.slide.IndexSlideBean;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:26:11
 */
public class BackendAction extends AbstractAjaxRequestHandle {
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
				CustomBean customBean = BackendUtils.applicationModule.getBeanByExp(CustomBean.class, "name=? and type=?", new Object[] { name, name });
				if (customBean == null) {
					customBean = new CustomBean();
					customBean.setType(name);
					customBean.setName(name);
				}
				customBean.setValue(sb.toString());
				BackendUtils.applicationModule.doUpdate(customBean);
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
				IndexSlideBean navBean = BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId);
				if (navBean == null) {
					navBean = new IndexSlideBean();
					navBean.setOorder(BackendUtils.applicationModule.nextId());
					navBean.setImage("");
				}
				navBean.setTitle(title);
				navBean.setUrl(url);
				BackendUtils.applicationModule.doUpdate(navBean);
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
				BackendUtils.applicationModule.doDelete(IndexSlideBean.class, navId);
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
				final IndexSlideBean navBean1 =BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId1);
				final IndexSlideBean navBean2 =BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId2);
				final boolean up = LangUtils.toBoolean(compParameter.getParameter("up"), false);
				BackendUtils.applicationModule.getDataObjectManager(IndexSlideBean.class).exchange(navBean1, navBean2, new Column("oorder"), up);
			}
		});
	}
}
