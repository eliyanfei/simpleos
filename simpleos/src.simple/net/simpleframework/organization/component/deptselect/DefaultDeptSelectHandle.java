package net.simpleframework.organization.component.deptselect;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.dictionary.AbstractDictionaryHandle;
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
public class DefaultDeptSelectHandle extends AbstractDictionaryHandle implements IDeptSelectHandle {

	protected Collection<? extends IDepartment> getDepartmentChildren(
			final ComponentParameter compParameter, final IDepartment parent) {
		return OrgUtils.dm().children(parent);
	}

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode) {
		IDepartment parent = null;
		if (treeNode != null) {
			parent = (IDepartment) treeNode.getDataObject();
		}
		final Collection<? extends IDepartment> departments = getDepartmentChildren(compParameter,
				parent);
		if (departments != null) {
			final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
			final ArrayList<AbstractTreeNode> al = new ArrayList<AbstractTreeNode>();
			for (final IDepartment department : departments) {
				final TreeNode tn = new TreeNode(treeBean, treeNode, department);
				tn.setImage(imgBase + "dept.png");
				al.add(tn);
			}
			return al;
		} else {
			return null;
		}
	}
}
