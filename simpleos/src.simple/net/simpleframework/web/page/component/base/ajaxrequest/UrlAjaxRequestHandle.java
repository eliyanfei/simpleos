package net.simpleframework.web.page.component.base.ajaxrequest;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UrlAjaxRequestHandle extends AbstractAjaxRequestHandle {

	@Override
	public IForward ajaxProcess(final ComponentParameter compParameter) {
		return new UrlForward(PageUtils.doPageUrl(compParameter,
				((AjaxRequestBean) compParameter.componentBean).getUrlForward()));
	}
}
