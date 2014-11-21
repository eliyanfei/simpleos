package net.simpleframework.organization.component.jobchartselect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobChartTree extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				(JobChartSelectBean) treeBean.getAttribute("__jobChartSelect"));
		return ((IJobChartSelectHandle) nComponentParameter.getComponentHandle()).getTreenodes(
				nComponentParameter, treeBean, treeNode);
	}

	@Override
	public Map<String, Object> getTreenodeAttributes(final ComponentParameter compParameter,
			final AbstractTreeNode treeNode) {
		final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("jobChartId", OrgUtils.jcm().getJobChartIdParameterName());
		return attributes;
	}
}
