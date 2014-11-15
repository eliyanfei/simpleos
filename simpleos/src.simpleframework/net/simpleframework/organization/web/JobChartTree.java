package net.simpleframework.organization.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.WebUtils;
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
public class JobChartTree extends AbstractTreeHandle implements IOrgConst {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final Collection<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
		if (treeNode == null) {
			TreeNode treeNode2 = new TreeNode(
					treeBean,
					treeNode,
					"#(jobchart_c.0)<br/><a class=\"addbtn a2\" onclick=\"$Actions['jcEditWindow']();\">#(jobchart_c.1)</a>");
			treeNode2.setContextMenu("jcContextMenu");
			treeNode2.setOpened(true);
			treeNode2.setImage(imgBase + "chart_g.png");
			nodes.add(treeNode2);

			treeNode2 = new TreeNode(treeBean, treeNode, "#(jobchart_c.2)");
			treeNode2.setContextMenu("jcCommonContextMenu");
			treeNode2.setOpened(true);
			treeNode2.setImage(imgBase + "chart_d.png");
			nodes.add(treeNode2);
		} else {
			final String image = treeNode.getImage();
			if (image.endsWith("chart_g.png")) {
				addJobChart(compParameter, treeNode, null, nodes);
			} else if (image.endsWith("chart_d.png")) {
				addDepartment(compParameter, treeNode, null, nodes);
			} else if (image.endsWith("dept.png")) {
				final IDepartment parent = treeNode != null ? (IDepartment) treeNode.getDataObject()
						: null;
				if (parent != null) {
					addJobChart(compParameter, treeNode, parent, nodes);
					addDepartment(compParameter, treeNode, parent, nodes);
				}
			}
		}
		return nodes;
	}

	private void addDepartment(final ComponentParameter compParameter,
			final AbstractTreeNode treeNode, final IDepartment parent,
			final Collection<AbstractTreeNode> nodes) {
		final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		for (final IDepartment department : OrgUtils.dm().children(parent)) {
			final AbstractTreeNode node = new TreeNode(treeBean, treeNode, department);
			nodes.add(node);
			node.setId("d_" + department.getId());
			node.setImage(imgBase + "dept.png");
			node.setContextMenu("jcContextMenu");
		}
	}

	private void addJobChart(final ComponentParameter compParameter,
			final AbstractTreeNode treeNode, final IDepartment parent,
			final Collection<AbstractTreeNode> nodes) {
		final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final IDataObjectQuery<? extends IJobChart> qs = OrgUtils.jcm().jobCharts(parent);
		IJobChart jc;
		while ((jc = qs.next()) != null) {
			final TreeNode treeNode2 = new TreeNode(treeBean, treeNode, jc);
			nodes.add(treeNode2);
			treeNode2.setContextMenu("jcContextMenu2");
			treeNode2.setImage(imgBase + "chart.png");
			treeNode2.setJsClickCallback("__jobmgr(branch);");
			final Integer c = getJobStat().get(jc.getId());
			treeNode2.setPostfixText(WebUtils.enclose(c == null ? 0 : c));
		}
	}

	private Map<Object, Integer> getJobStat() {
		@SuppressWarnings("unchecked")
		Map<Object, Integer> jobStat = (Map<Object, Integer>) OrgUtils.jm()
				.getAttribute("__job_stat");
		if (jobStat == null) {
			jobStat = new HashMap<Object, Integer>();
			final IQueryEntitySet<Map<String, Object>> qs = OrgUtils.getQueryEntityManager().query(
					new SQLValue("select count(*) as jc, jobchartid from " + OrgUtils.jm().tblname()
							+ " group by jobchartid"));
			Map<String, Object> m;
			while ((m = qs.next()) != null) {
				jobStat.put(OrgUtils.jm().id(m.get("jobchartid")), ConvertUtils.toInt(m.get("jc"), 0));
			}
		}
		return jobStat;
	}

	static {
		OrgUtils.jm().addListener(new StatTableEntityAdapter() {
			@Override
			void resetStat() {
				OrgUtils.jm().removeAttribute("__job_stat");
			}
		});
	}
}
