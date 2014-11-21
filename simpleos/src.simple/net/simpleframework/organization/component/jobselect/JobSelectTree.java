package net.simpleframework.organization.component.jobselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobSelectTree extends AbstractTreeHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("containerId".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = JobSelectUtils
					.getComponentParameter(compParameter);
			if (nComponentParameter.componentBean != null) {
				return "container_" + nComponentParameter.componentBean.hashId();
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, JobSelectUtils.BEAN_ID);
		return parameters;
	}

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
		final ComponentParameter nComponentParameter = JobSelectUtils
				.getComponentParameter(compParameter);
		final IJobChart jobChart = OrgUtils.jcm().queryForObjectById(
				compParameter.getRequestParameter(OrgUtils.jcm().getJobChartIdParameterName()));
		final IJobSelectHandle jsHandle = (IJobSelectHandle) nComponentParameter.getComponentHandle();
		final Collection<TreeNode> coll = new ArrayList<TreeNode>();
		final IJob parent = treeNode == null ? null : (IJob) treeNode.getDataObject();
		final Collection<? extends IJob> jobs = jsHandle.getJobs(nComponentParameter, jobChart,
				parent);
		if (jobs != null) {
			for (final IJob job : jobs) {
				final TreeNode treeNode2 = new TreeNode(treeBean, treeNode, job);
				treeNode2.setImage(imgBase + "job.png");
				treeNode2.setOpened(true);
				treeNode2.setJsDblclickCallback("selected" + nComponentParameter.componentBean.hashId()
						+ "(branch, ev);");
				coll.add(treeNode2);
			}
		}
		return coll;
	}
}
