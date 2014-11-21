package net.simpleframework.organization.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.db.Column;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.UrlForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobChartAction extends AbstractAjaxRequestHandle {

	private IJobChart getJobChartByRequest(final HttpServletRequest request) {
		return OrgUtils.jcm().queryForObjectById(
				request.getParameter(OrgUtils.jcm().getJobChartIdParameterName()));
	}

	public IForward jobChartSave(final ComponentParameter compParameter) {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				IJobChart jobChart = getJobChartByRequest(request);
				boolean insert = false;
				if (jobChart == null) {
					jobChart = OrgUtils.jcm().createJobChart(null, null, null);
					insert = true;
				}
				final String departmentId = request.getParameter(OrgUtils.dm()
						.getDepartmentIdParameterName());
				if (StringUtils.hasText(departmentId)) {
					jobChart.setDepartmentId(OrgUtils.dm().id(departmentId));
				}
				PageUtils.setObjectFromRequest(jobChart, request, "jc_", new String[] { "name", "text",
						"description" });
				if (insert) {
					OrgUtils.jcm().getEntityManager().insertTransaction(jobChart);
				} else {
					OrgUtils.jcm().getEntityManager().updateTransaction(jobChart);
				}
				json.put("next", ConvertUtils.toBoolean(request.getParameter("next"), false));
			}
		});
	}

	public IForward jcMove(final ComponentParameter compParameter) {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final Object id = OrgUtils.jcm().queryForObjectById(request.getParameter("b1"));
				final Object id2 = OrgUtils.jcm().queryForObjectById(request.getParameter("b2"));
				final boolean up = ConvertUtils.toBoolean(request.getParameter("up"), false);
				OrgUtils.jcm().getEntityManager().exchange(id, id2, new Column("oorder"), !up);
			}
		});
	};

	public IForward jcDelete(final ComponentParameter compParameter) {
		final HttpServletRequest request = compParameter.request;
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IJobChart jobChart = getJobChartByRequest(request);
				if (jobChart != null) {
					if (OrgUtils.jm().children(jobChart).size() > 0) {
						throw HandleException.wrapException(LocaleI18n.getMessage("JobChartAction.0"));
					}
					OrgUtils.jcm()
							.delete(new ExpressionValue("id=?", new Object[] { jobChart.getId() }));
				}
			}
		});
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			final String componentName = (String) compParameter.getBeanProperty("name");
			if ("ajaxJobChartEdit".equals(componentName) || "ajaxJobChartMove".equals(componentName)
					|| "ajaxJobChartDelete".equals(componentName)) {
				return OrgUtils.applicationModule.getManager(compParameter);
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public IForward jcEditUrl(final ComponentParameter compParameter) {
		return new UrlForward(OrgUtils.deployPath + "jsp/job/jobchart_edit.jsp");
	}
}
