package net.simpleframework.web.page.component.ui.pager;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PagerAction extends AbstractAjaxRequestHandle {

	@Override
	public IForward ajaxProcess(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = PagerUtils
				.getComponentParameter(compParameter);
		final String pageNumberParameterName = (String) nComponentParameter
				.getBeanProperty("pageNumberParameterName");
		final int pageNumber = Math
				.max(ConvertUtils.toInt(
						nComponentParameter.getRequestParameter(pageNumberParameterName), 0), 1);
		PagerUtils.setPageAttributes(nComponentParameter, pageNumberParameterName, pageNumber);
		final String pageItemsParameterName = (String) nComponentParameter
				.getBeanProperty("pageItemsParameterName");
		final int pageItems = ConvertUtils.toInt(
				nComponentParameter.getRequestParameter(pageItemsParameterName), 0);
		if (pageItems > 0) {
			PagerUtils.setPageAttributes(nComponentParameter, pageItemsParameterName, pageItems);
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(PagerUtils.getHomePath()).append("/jsp/pager.jsp");
		final String xpParameter = PagerUtils.getXmlPathParameter(nComponentParameter);
		if (StringUtils.hasText(xpParameter)) {
			sb.append("?").append(xpParameter);
		}
		return new UrlForward(sb.toString());
	}
}
