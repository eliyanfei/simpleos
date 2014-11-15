package net.itsite.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

public class DefaultPlatformRoleTreeHandle extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final Collection<AbstractTreeNode> _treeNodes = new ArrayList<AbstractTreeNode>();
		if (treeNode != null)
			return null;
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		for (final String view : PlatformConstants.views) {
			final IJobChart jobChart = OrgUtils.jcm().getJobChartByName(view);
			if (jobChart == null) {
				continue;
			}
			final TreeNode jobCharTreeNode = new TreeNode(treeBean, treeNode, jobChart);
			jobCharTreeNode.setOpened(true);
			_treeNodes.add(jobCharTreeNode);
			final Collection<IJob> jobs = OrgUtils.jm().children(jobChart);
			final Iterator<IJob> jobIterator = jobs.iterator();
			while (jobIterator.hasNext()) {
				final IJob job = jobIterator.next();
				final TreeNode jobTreeNode = new TreeNode(treeBean, jobCharTreeNode, job);
				jobTreeNode.setJsClickCallback(getJsClickCallback(job));
				jobCharTreeNode.getChildren().add(jobTreeNode);
			}
		}
		return _treeNodes;
	}

	public static final String action = "menuTreeRef";
	public static final String jobNameInput = "jobName";
	public static final String jobIdInput = "jobId";
	public static final String treeContainer = "treeContainer";

	protected String getJsClickCallback(final IJob job) {
		final StringBuffer buffer = new StringBuffer();

		buffer.append("$('").append(treeContainer).append("')");
		buffer.append(".style.display='';");

		buffer.append("$('").append(jobNameInput).append("')");
		buffer.append(".value='");
		buffer.append(job.getName());
		buffer.append("';");

		buffer.append("$('").append(jobIdInput).append("')");
		buffer.append(".value='");
		buffer.append(job.getId().getValue());
		buffer.append("';");

		buffer.append("var treeAct =  $Actions['" + action + "'];");
		buffer.append("treeAct.refresh('jobId=");
		buffer.append(job.getId().getValue());
		buffer.append("');");
		return wrapFunction(buffer.toString());
	}

	public static String wrapFunction(final String content) {
		final StringBuilder builder = new StringBuilder();
		builder.append("(function(){");
		builder.append(StringUtils.text(content));
		builder.append("})();");
		return builder.toString();
	}

}
