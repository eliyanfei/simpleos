package net.simpleframework.organization.component.register;

import net.simpleframework.organization.IJob;
import net.simpleframework.web.page.component.AbstractComponentHtmlRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserRegisterRender extends AbstractComponentHtmlRender {
	public UserRegisterRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/regist.jsp?").append(UserRegisterUtils.BEAN_ID);
		sb.append("=").append(compParameter.componentBean.hashId());
		return sb.toString();
	}

	@Override
	protected void initAjaxRequestBean(final ComponentParameter compParameter,
			final AjaxRequestBean ajaxRequest) {
		super.initAjaxRequestBean(compParameter, ajaxRequest);
		ajaxRequest.setJobExecute(IJob.sj_anonymous);
	}
}
