package net.simpleframework.web.page.component.ui.pager;

import java.util.Map;

import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.base.ajaxrequest.AjaxRequestBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerAction extends AbstractAjaxRequestHandle {

	public IForward doExport(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final StringBuilder sb = new StringBuilder();
				sb.append(PagerUtils.getHomePath()).append("/jsp/tablepager_export_dl.jsp?");
				sb.append(AbstractUrlForward.putRequestData(compParameter, "p"));
				sb.append("&filetype=").append(compParameter.getRequestParameter("filetype"));
				sb.append("&v=").append(compParameter.getRequestParameter("v"));
				json.put("dl", sb.toString());
			}
		});
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = PagerUtils.getComponentParameter(compParameter);
			return nComponentParameter.getBeanProperty("selector") + ", " + super.getBeanProperty(compParameter, beanProperty);
		} else if ("showLoading".equals(beanProperty) || "loadingModal".equals(beanProperty)) {
			final String method = ((AjaxRequestBean) compParameter.componentBean).getHandleMethod();
			if ("doExport".equals(method) || "doFilter".equals(method) || "doFilterDelete".equals(method)) {
				final ComponentParameter nComponentParameter = PagerUtils.getComponentParameter(compParameter);
				return nComponentParameter.getBeanProperty(beanProperty);
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward filterUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(TablePagerRegistry.tablePager, "/jsp/tablepager_filter.jsp");
	}

	public IForward doFilter(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final ComponentParameter nComponentParameter = PagerUtils.getComponentParameter(compParameter);
		sb.append("$Actions['").append(nComponentParameter.getBeanProperty("name")).append("']('").append(TablePagerUtils.PAGER_FILTER_CUR_COL)
				.append("=");
		sb.append(compParameter.getRequestParameter(TablePagerUtils.PAGER_FILTER_CUR_COL)).append("&").append(TablePagerUtils.PAGER_FILTER)
				.append("=");
		sb.append(compParameter.getRequestParameter("tp_filter_r1"));
		sb.append(";").append(compParameter.getRequestParameter("tp_filter_v1"));
		//		sb.append(";").append(StringUtils.encodeHex(compParameter.getRequestParameter("tp_filter_v1").getBytes()));
		final String op = compParameter.getRequestParameter("tp_filter_op");
		if ("and".equals(op) || "or".equals(op)) {
			sb.append(";").append(op).append(";");
			sb.append(compParameter.getRequestParameter("tp_filter_r2"));
			sb.append(";").append(compParameter.getRequestParameter("tp_filter_v2"));
			//			sb.append(";").append(StringUtils.encodeHex(compParameter.getRequestParameter("tp_filter_v2").getBytes()));
		}
		sb.append("');");
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				json.put("filter", sb.toString());
			}
		});
	}

	public IForward doFilterDelete(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final ComponentParameter nComponentParameter = PagerUtils.getComponentParameter(compParameter);
		sb.append("$Actions['").append(nComponentParameter.getBeanProperty("name")).append("']('").append(TablePagerUtils.PAGER_FILTER_CUR_COL)
				.append("=").append(compParameter.getRequestParameter(TablePagerUtils.PAGER_FILTER_CUR_COL)).append("&")
				.append(TablePagerUtils.PAGER_FILTER).append("=none');");
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				json.put("filter", sb.toString());
			}
		});
	}
}
