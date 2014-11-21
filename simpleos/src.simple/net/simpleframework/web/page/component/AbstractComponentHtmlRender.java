package net.simpleframework.web.page.component;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestRender;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractComponentHtmlRender extends AbstractComponentRender implements
		IComponentHtmlRender {
	public AbstractComponentHtmlRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getHtml(final ComponentParameter compParameter) {
		return getResponseText(compParameter);
	}

	public String getResponseText(final ComponentParameter compParameter) {
		final String IncludeRequestData = StringUtils.text(
				compParameter.componentBean.getIncludeRequestData(), "p");
		return new UrlForward(getResponseUrl(compParameter), IncludeRequestData)
				.getResponseText(compParameter);
	}

	public String getResponseUrl(final ComponentParameter compParameter) {
		return compParameter.componentBean.getResourceHomePath() + getRelativePath(compParameter);
	}

	protected abstract String getRelativePath(final ComponentParameter compParameter);

	@Override
	public String getHtmlJavascriptCode(final ComponentParameter compParameter) {
		return wrapHtmlAjaxCode(compParameter);
	}

	protected String getAJAXScript_ID(final AbstractComponentBean componentBean) {
		return "AJAX$" + componentBean.hashId();
	}

	protected String createAjaxRequestJavascriptCode(final ComponentParameter compParameter) {
		final AbstractContainerBean containerBean = (AbstractContainerBean) compParameter.componentBean;
		final String ajaxRequestName = "ajaxRequest_" + containerBean.hashId();
		AjaxRequestBean ajaxRequestBean = (AjaxRequestBean) compParameter
				.getComponentBean(ajaxRequestName);
		if (ajaxRequestBean == null) {
			final PageDocument pageDocument = compParameter.getPageDocument();
			ajaxRequestBean = new AjaxRequestBean(pageDocument, null);
			ajaxRequestBean.setAjaxRequestId(getAJAXScript_ID(containerBean));
			ajaxRequestBean.setName(ajaxRequestName);
			pageDocument.addComponentBean(ajaxRequestBean);
		}
		initAjaxRequestBean(compParameter, ajaxRequestBean);

		final StringBuilder sb = new StringBuilder();
		sb.append(AjaxRequestRender.render(ComponentParameter.get(compParameter, ajaxRequestBean)));
		sb.append("var cf = $Actions[\"").append(compParameter.getBeanProperty("name"))
				.append("\"];");
		sb.append("var af = $Actions[\"").append(ajaxRequestBean.getName()).append("\"];");
		sb.append("af.container = cf.container;");
		sb.append("af.selector = cf.selector;");
		sb.append("af(arguments[0]);");
		return sb.toString();
	}

	protected void initAjaxRequestBean(final ComponentParameter compParameter,
			final AjaxRequestBean ajaxRequest) {
		ajaxRequest.setUrlForward(getResponseUrl(compParameter));
		ajaxRequest.setUpdateContainerId((String) compParameter.getBeanProperty("containerId"));
		ajaxRequest.setSelector((String) compParameter.getBeanProperty("selector"));
	}

	protected String wrapHtmlAjaxCode(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.wrapActionFunction(compParameter,
				createAjaxRequestJavascriptCode(compParameter), null, false));
		// refresh函数
		final String actionFunc = compParameter.componentBean.getActionFunction();
		sb.append(actionFunc).append(".refresh = function(params) {");
		sb.append(actionFunc).append("(params);");
		sb.append("};");
		return sb.toString();
	}
}
