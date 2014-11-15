package net.simpleframework.organization.component.userpager;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 显示查询的树结构
 */
public class HometownCatalogTreeHandle extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		String exp = "1<>1";
		if (treeNode == null) {
			SysDict sysDict = DictUtils.getSysDictByName("district");
			if (sysDict == null) {
				return null;
			}
			exp = "parentid=0 and documentId=" + sysDict.getId() + " order by oorder";
		} else {
			exp = "parentid=" + treeNode.getId();
		}
		final IQueryEntitySet<SysDict> catalogs = DictUtils.getTableEntityManager(SysDict.class).query(new ExpressionValue(exp), SysDict.class);
		if (catalogs != null) {
			final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
			SysDict sd = null;
			while ((sd = catalogs.next()) != null) {
				final TreeNode treeNode2 = new TreeNode((TreeBean) compParameter.componentBean, treeNode, sd);
				treeNode2.setText(sd.getText());
				treeNode2.setAttribute("id", sd.getId());
				nodes.add(treeNode2);
			}
			return nodes;
		}
		return null;
	}

}
