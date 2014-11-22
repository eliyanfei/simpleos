package net.simpleos.module.docu;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 显示查询的树结构
 */
public class DocuCatalogTreeHandle extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
		final IQueryEntitySet<DocuCatalog> catalogs = DocuUtils.applicationModule
				.queryCatalogs(treeNode == null ? null : treeNode.getAttribute("id"));
		if (catalogs.getCount() > 0) {
			DocuCatalog wc = null;
			while ((wc = catalogs.next()) != null) {
				final TreeNode treeNode2 = new TreeNode((TreeBean) compParameter.componentBean, treeNode, wc);
				treeNode2.setText(wc.getText());
				treeNode2.setAttribute("id", wc.getId());
				treeNode2.setJsClickCallback("$IT.A('documentPaperAct','catalogId=" + wc.getId() + "');");
				if (treeNode != null) {
					treeNode2.setId(treeNode.getText() + "-" + treeNode2.getText() + "|" + wc.getId());
				}
				nodes.add(treeNode2);
			}
			return nodes;
		}
		return nodes;
	}
}
