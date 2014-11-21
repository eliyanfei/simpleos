package net.simpleframework.organization.component.jobselect;

import java.util.Collection;

import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.AbstractDictionaryHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultJobSelectHandle extends AbstractDictionaryHandle implements IJobSelectHandle {

	@Override
	public IJobChart getDefaultJobChart(final ComponentParameter compParameter) {
		return OrgUtils.jcm().getJobChartByName(IJobChart.sysjc);
	}

	@Override
	public Collection<? extends IJob> getJobs(final ComponentParameter compParameter,
			IJobChart jobChart, final IJob parent) {
		if (jobChart == null) {
			jobChart = getDefaultJobChart(compParameter);
		}
		if (parent == null) {
			return OrgUtils.jm().children(jobChart);
		} else {
			return OrgUtils.jm().children(parent);
		}
	}
}
