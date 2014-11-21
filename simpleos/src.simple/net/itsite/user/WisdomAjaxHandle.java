package net.itsite.user;

import java.util.Arrays;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

public class WisdomAjaxHandle extends AbstractAjaxRequestHandle {
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_manager;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward deleteWisdom(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String ids = compParameter.getRequestParameter("wisdomIds");
				if (StringUtils.hasText(ids)) {
					final ITableEntityManager tMgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, WisdomBean.class);
					final String ids_ = StringUtils.join(Arrays.asList(ids.split(",")), ",");
					final ExpressionValue eValue = new ExpressionValue("id in(" + ids_ + ")", null);
					tMgr.deleteTransaction(eValue);
				}
			}
		});
	}

	public IForward wisdomSave(final ComponentParameter compParameter) throws Exception {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				final ITableEntityManager tMgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, WisdomBean.class);
				WisdomBean wisdomBean = new WisdomBean();
				wisdomBean.setType(ConvertUtils.toInt(compParameter.getRequestParameter("ws_type"), 0));
				wisdomBean.setContent(compParameter.getRequestParameter("ws_content"));
				tMgr.insert(wisdomBean);
			}
		});
	}
}
