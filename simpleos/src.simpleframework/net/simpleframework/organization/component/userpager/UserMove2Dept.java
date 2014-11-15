package net.simpleframework.organization.component.userpager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.component.deptselect.DefaultDeptSelectHandle;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.ComponentParameter;
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
public class UserMove2Dept extends DefaultDeptSelectHandle {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, UserPagerUtils.BEAN_ID);
		return parameters;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Collection<IDepartment> getDepartmentChildren(final ComponentParameter compParameter,
			final IDepartment parent) {
		final ComponentParameter nComponentParameter = UserPagerUtils
				.getComponentParameter(compParameter);
		return (Collection<IDepartment>) ((IUserPagerHandle) nComponentParameter.getComponentHandle())
				.getMove2Catalogs(nComponentParameter, parent);
	}

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode) {
		if (treeNode == null) {
			final String imgBase = OrgUtils.getCssPath(compParameter) + "/images/";
			final Collection<AbstractTreeNode> coll = new ArrayList<AbstractTreeNode>();
			final AbstractTreeNode tn1 = new TreeNode(treeBean, treeNode, null);
			tn1.setId("none");
			tn1.setText(LocaleI18n.getMessage("up_pager.1"));
			tn1.setImage(imgBase + "users_nodept.png");
			coll.add(tn1);
			final AbstractTreeNode tn2 = new TreeNode(treeBean, treeNode, null);
			tn2.setId("root");
			tn2.setText(LocaleI18n.getMessage("up_pager.2"));
			tn2.setImage(imgBase + "dept_root.png");
			tn2.setOpened(true);
			coll.add(tn2);
			return coll;
		} else {
			if (!"none".equals(treeNode.getId())) {
				return super.getTreenodes(compParameter, treeBean, treeNode);
			} else {
				return null;
			}
		}
	}
}
