package net.simpleframework.organization.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrgEditPageLoad extends DefaultPageHandle {

	public void jobChartLoaded(final PageParameter pageParameter,
			final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final HttpServletRequest request = pageParameter.request;
		final IJobChart jc = OrgUtils.jcm().queryForObjectById(
				request.getParameter(OrgUtils.jcm().getJobChartIdParameterName()));
		if (jc != null) {
			dataBinding.put("jc_text", jc.getText());
			dataBinding.put("jc_name", jc.getName());
			dataBinding.put("jc_description", jc.getDescription());
		}
	}
}
