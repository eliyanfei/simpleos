package net.simpleframework.web.page.component.base.jspinclude;

import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.AbstractComponentHtmlRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JspIncludeRender extends AbstractComponentHtmlRender {

	public JspIncludeRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getHtml(final ComponentParameter compParameter) {
		final JspIncludeBean jspInclude = (JspIncludeBean) compParameter.componentBean;

		final IJspIncludeHandle jHandle = (IJspIncludeHandle) compParameter.getComponentHandle();
		if (jHandle != null) {
			final IForward forward = jHandle.jspInclude(compParameter);
			if (forward != null) {
				return forward.getResponseText(compParameter);
			}
		}
		final String includePath = (String) compParameter.getBeanProperty("page");
		return new UrlForward(includePath, jspInclude.getIncludeRequestData())
				.getResponseText(compParameter);
	}

	@Override
	protected String getRelativePath(final ComponentParameter compParameter) {
		return null;
	}
}
