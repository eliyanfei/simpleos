package net.simpleframework.web.page.component.base.ajaxrequest;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
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
public class AjaxRequestRender extends AbstractComponentJavascriptRender {
	public AjaxRequestRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		return render(compParameter);
	}

	public static String render(final ComponentParameter compParameter) {
		final AjaxRequestBean ajaxRequestBean = (AjaxRequestBean) compParameter.componentBean;
		final String actionFunc = ajaxRequestBean.getActionFunction();
		final StringBuilder sb = new StringBuilder();
		final String containerId = (String) compParameter.getBeanProperty("updateContainerId");
		sb.append("if (").append(actionFunc).append(".doInit(");
		if (StringUtils.hasText(containerId)) {
			sb.append("\"").append(containerId).append("\", ");
		} else {
			sb.append("null, ");
		}
		final String confirmMessage = (String) compParameter.getBeanProperty("confirmMessage");
		if (StringUtils.hasText(confirmMessage)) {
			sb.append("\"").append(JavascriptUtils.escape(confirmMessage)).append("\", ");
		} else {
			sb.append("null, ");
		}

		sb.append(compParameter.getBeanProperty("parallel")).append(", ")
				.append(compParameter.getBeanProperty("showLoading")).append(", ")
				.append(compParameter.getBeanProperty("loadingModal")).append(", ")
				.append(compParameter.getBeanProperty("disabledTriggerAction"));
		sb.append(")) return;");

		// doComplete
		sb.append("function dc(req) { ").append(actionFunc).append(".doLoaded(req); };");

		final String callback = (String) compParameter.getBeanProperty("jsCompleteCallback");
		if (StringUtils.hasText(callback)) {
			sb.append(actionFunc).append(".doCallback = function(req, responseText, json) {");
			sb.append(callback);
			sb.append("};");
		}

		sb.append("var p=\"").append(AjaxRequestUtils.BEAN_ID);
		sb.append("=").append(ajaxRequestBean.hashId()).append("\";");
		appendParameters(sb, compParameter, "p");
		sb.append("p = p.addParameter(arguments[0]);");
		sb.append("new Ajax.Request(\"");
		if(StringUtils.hasText(compParameter.getRequestParameter("url"))){
			sb.append(compParameter.getRequestParameter("url")).append(ajaxRequestBean.getResourceHomePath(compParameter)).append(
			"/jsp/ajax_request.jsp\", {");
		}else{
		sb.append(ajaxRequestBean.getResourceHomePath(compParameter)).append(
				"/jsp/ajax_request.jsp\", {");
		}
		sb.append("postBody: p,");

		final String encoding = (String) compParameter.getBeanProperty("encoding");
		if (StringUtils.hasText(encoding)) {
			sb.append("encoding: \"").append(encoding).append("\",");
		}
		sb.append("onComplete: function(req) {");
		sb.append(actionFunc).append(".doComplete(req, ");
		if (StringUtils.hasText(containerId)) {
			sb.append("\"").append(containerId).append("\", ");
		} else {
			sb.append("null, ");
		}

		sb.append(compParameter.getBeanProperty("updateContainerCache")).append(", ");
		final EThrowException throwException = (EThrowException) compParameter
				.getBeanProperty("throwException");
		sb.append(throwException == EThrowException.alert);
		sb.append(");");
		sb.append("}, onException: dc, onFailure: dc, on404: dc });");
		final StringBuilder sb2 = new StringBuilder();
		sb2.append("__ajax_actions_init(").append(actionFunc).append(", '")
				.append(compParameter.getBeanProperty("name")).append("');");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString(), sb2.toString());
	}
}
