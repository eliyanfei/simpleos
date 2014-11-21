package net.simpleframework.organization.component.jobselect;

import java.util.Collection;

import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.IDictionaryHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IJobSelectHandle extends IDictionaryHandle {

	IJobChart getDefaultJobChart(ComponentParameter compParameter);

	Collection<? extends IJob> getJobs(ComponentParameter compParameter, IJobChart jobChart,
			IJob parent);
}