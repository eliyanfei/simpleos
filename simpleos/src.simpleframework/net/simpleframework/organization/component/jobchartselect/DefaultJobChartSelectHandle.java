package net.simpleframework.organization.component.jobchartselect;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.AbstractDictionaryHandle;
import net.simpleframework.web.page.component.ui.tooltip.TipBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultJobChartSelectHandle extends AbstractDictionaryHandle implements
		IJobChartSelectHandle {
	@Override
	public Collection<? extends IJobChart> getJobCharts(final ComponentParameter compParameter,
			final IDepartment department) {
		return IDataObjectQuery.Utils.toList(OrgUtils.jcm().jobCharts(department));
	}

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode) {
		final Collection<AbstractTreeNode> coll = new ArrayList<AbstractTreeNode>();
		final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
		if (treeNode == null) {
			TreeNode treeNode2 = new TreeNode(treeBean, treeNode,
					LocaleI18n.getMessage("DefaultJobChartSelectHandle.0"));
			treeNode2.setId("chart_g");
			treeNode2.setImage(imgBase + "chart_g.png");
			coll.add(treeNode2);

			treeNode2 = new TreeNode(treeBean, treeNode,
					LocaleI18n.getMessage("DefaultJobChartSelectHandle.1"));
			treeNode2.setId("chart_d");
			treeNode2.setImage(imgBase + "chart_d.png");
			coll.add(treeNode2);
		} else {
			final Object dataObject = treeNode.getDataObject();
			if (dataObject instanceof IDepartment) {
				final IDepartment parent = (IDepartment) dataObject;
				final IDataObjectQuery<? extends IJobChart> qs = OrgUtils.jcm().jobCharts(parent);
				if (qs.getCount() > 0) {
					IJobChart jobChart;
					while ((jobChart = qs.next()) != null) {
						final TreeNode treeNode2 = new TreeNode(treeBean, treeNode, jobChart);
						treeNode2.setId(String.valueOf(jobChart.getId()));
						coll.add(treeNode2);
					}
				}
				for (final IDepartment department : OrgUtils.dm().children(parent)) {
					coll.add(new TreeNode(treeBean, treeNode, department));
				}
			} else {
				final Object id = treeNode.getId();
				if ("chart_g".equals(id)) {
					for (final IJobChart jobChart : getJobCharts(compParameter, null)) {
						final TreeNode treeNode2 = new TreeNode(treeBean, treeNode, jobChart);
						treeNode2.setId(String.valueOf(jobChart.getId()));
						coll.add(treeNode2);
					}
				} else if ("chart_d".equals(id)) {
					for (final IDepartment department : OrgUtils.dm().root()) {
						coll.add(new TreeNode(treeBean, treeNode, department));
					}
				}
			}
		}
		for (final AbstractTreeNode treeNode2 : coll) {
			final Object dataObject = treeNode2.getDataObject();
			if (!(dataObject instanceof IJobChart)) {
				treeNode2.setJsDblclickCallback("(function() { return false; })();");
				if (dataObject instanceof IDepartment) {
					treeNode2.setImage(imgBase + "dept.png");
				}
			} else {
				treeNode2.setImage(imgBase + "chart.png");
			}
		}
		return coll;
	}

	@Override
	public void doTipBean(final ComponentParameter compParameter, final TipBean tip) {
		String content = tip.getContent();
		content += LocaleI18n.getMessage("DefaultJobChartSelectHandle.2",
				OrgUtils.getCssPath(compParameter) + "/images/chart.png");
		tip.setContent(content);
	}
}
