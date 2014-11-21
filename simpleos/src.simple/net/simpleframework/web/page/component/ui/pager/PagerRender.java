package net.simpleframework.web.page.component.ui.pager;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentHtmlRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestRender;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PagerRender extends AbstractComponentHtmlRender {
	public PagerRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/pager.jsp?").append(getBeanIdName(compParameter));
		sb.append("=").append(compParameter.componentBean.hashId());
		return sb.toString();
	}

	private String getBeanIdName(final ComponentParameter compParameter) {
		final AbstractPagerHandle pHandle = (AbstractPagerHandle) compParameter.getComponentHandle();
		return pHandle != null ? pHandle.getBeanIdName() : PagerUtils.BEAN_ID;
	}

	@Override
	public String getResponseUrl(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(PagerUtils.BEAN_ID_NAME).append("=").append(getBeanIdName(compParameter));
		final String xpParameter = PagerUtils.getXmlPathParameter(compParameter);
		if (StringUtils.hasText(xpParameter)) {
			sb.append("&").append(xpParameter);
		}
		final String pageItemsParameterName = (String) compParameter.getBeanProperty("pageItemsParameterName");
		final String pageItems = compParameter.getRequestParameter(pageItemsParameterName);
		if (StringUtils.hasText(pageItems)) {
			sb.append("&").append(pageItemsParameterName);
			sb.append("=").append(pageItems);
		}
		return PageUtils.addParameters(PagerUtils.getHomePath() + getRelativePath(compParameter), sb.toString());
	}

	@Override
	public String getHtmlJavascriptCode(final ComponentParameter compParameter) {
		final PagerBean pagerBean = (PagerBean) compParameter.componentBean;
		pagerBean.setJsLoadedCallback("$IT.filter();" + pagerBean.getJsLoadedCallback());
		final StringBuilder sb = new StringBuilder();
		sb.append(wrapHtmlAjaxCode(compParameter));

		final PageDocument pageDocument = pagerBean.getPageDocument();
		final String ajaxRequestName = "__doPager";
		AjaxRequestBean ajaxRequestBean = (AjaxRequestBean) compParameter.getComponentBean(ajaxRequestName);
		if (ajaxRequestBean == null) {
			ajaxRequestBean = new AjaxRequestBean(pageDocument, null);
			ajaxRequestBean.setName(ajaxRequestName);
			ajaxRequestBean.setAjaxRequestId(getAJAXScript_ID(pagerBean));
			ajaxRequestBean.setHandleClass(PagerAction.class.getName());
			pageDocument.addComponentBean(ajaxRequestBean);
		}
		ajaxRequestBean.setShowLoading((Boolean) compParameter.getBeanProperty("showLoading"));
		ajaxRequestBean.setLoadingModal((Boolean) compParameter.getBeanProperty("loadingModal"));

		sb.append(AjaxRequestRender.render(ComponentParameter.get(compParameter, ajaxRequestBean)));

		sb.append(pagerBean.getActionFunction()).append(".doPager = function(to, params) {");
		sb.append("var p = to.up(\".pager\");");
		sb.append("var af = $Actions[\"__doPager\"];");
		sb.append("af.container = p.up();");
		sb.append("af.selector = \"");
		sb.append(compParameter.getBeanProperty("selector")).append("\";");
		sb.append("af(\"").append(getBeanIdName(compParameter)).append("=");
		sb.append(pagerBean.hashId()).append("\".addParameter(params));");
		sb.append("};");
		return sb.toString();
	}

	@Override
	protected void initAjaxRequestBean(final ComponentParameter compParameter, final AjaxRequestBean ajaxRequest) {
		super.initAjaxRequestBean(compParameter, ajaxRequest);

		ajaxRequest.setShowLoading((Boolean) compParameter.getBeanProperty("showLoading"));
		ajaxRequest.setLoadingModal((Boolean) compParameter.getBeanProperty("loadingModal"));
	}
}
