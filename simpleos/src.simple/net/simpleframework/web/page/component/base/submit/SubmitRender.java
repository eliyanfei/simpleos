package net.simpleframework.web.page.component.base.submit;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.ReflectUtils;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SubmitRender extends AbstractComponentJavascriptRender {

	public SubmitRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final SubmitBean submitBean = (SubmitBean) compParameter.componentBean;

		final StringBuilder sb = new StringBuilder();

		final String confirmMessage = submitBean.getConfirmMessage();
		if (StringUtils.hasText(confirmMessage)) {
			sb.append("if (!confirm(\"").append(JavascriptUtils.escape(confirmMessage));
			sb.append("\")) { return; }");
		}

		final String formName = submitBean.getFormName();
		sb.append("var form = $(\"").append(formName).append("\") || document.").append(formName)
				.append(";");
		sb.append("if (!form) return;");
		IForward forward = null;
		if (ReflectUtils.methodAccessForward != null) {
			final String jobExecute = (String) compParameter.getBeanProperty("jobExecute");
			try {
				forward = (IForward) ReflectUtils.methodAccessForward.invoke(null, compParameter,
						jobExecute, compParameter.getBeanProperty("name"));
			} catch (final Exception e) {
			}
		}
		if (forward != null) {
			sb.append("var win = parent || window;");
			sb.append("var ele = new Element('DIV'); $(win.document.body).insert(ele);");
			sb.append("new win.$Comp.AjaxRequest(ele, '")
					.append(JavascriptUtils.escape(forward.getResponseText(compParameter)))
					.append("', '").append(formName).append("');");
			sb.append("return;");
		}
		final String beanId = submitBean.hashId();
		sb.append("form.action=\"").append(getResourceHomePath(compParameter))
				.append("/jsp/submit.jsp?").append(SubmitUtils.BEAN_ID).append("=").append(beanId)
				.append("\";");
		sb.append("form.action = form.action.addParameter(arguments[0]);");
		sb.append("form.method=\"post\";");
		if (submitBean.isBinary()) {
			sb.append("form.encoding = \"multipart/form-data\";");
		}
		sb.append("form.submit();");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}
