package net.simpleframework.content.component.catalog;

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
public class CatalogRender extends AbstractComponentHtmlRender {
	public CatalogRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/catalog.jsp?").append(CatalogUtils.BEAN_ID);
		sb.append("=").append(compParameter.componentBean.hashId());
		return sb.toString();
	}

	@Override
	public String getHtmlJavascriptCode(final ComponentParameter compParameter) {
		final CatalogBean catalogBean = (CatalogBean) compParameter.componentBean;
		final StringBuilder sb = new StringBuilder();
		sb.append(wrapHtmlAjaxCode(compParameter));
		final String actionFunc = catalogBean.getActionFunction();
		sb.append(actionFunc).append(".getTree = function() { return $Actions[\"__catalogTree_");
		sb.append(catalogBean.hashId()).append("\"]; };");
		return sb.toString();
	}
}
