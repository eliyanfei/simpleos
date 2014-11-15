package net.prj.manager.template;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午03:26:11
 */
public class PrjTemplateAction extends AbstractAjaxRequestHandle {
	/**
	 * 保存自定义变量
	 * @param compParameter
	 * @return
	 */
	public IForward template(final ComponentParameter compParameter) {
		return new UrlForward("");
	}
}
