package net.itsite.user;

import java.util.ArrayList;
import java.util.Collection;

import net.a.ItSiteUtil;
import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 显示查询的树结构
 */
public class SchoolCatalogTreeHandle extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final IQueryEntitySet<SchoolCatalog> catalogs = queryCatalogs(compParameter,
				treeNode == null ? null : (ITreeBeanAware) treeNode.getDataObject());
		if (catalogs != null) {
			final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
			SchoolCatalog sc = null;
			while ((sc = catalogs.next()) != null) {
				final TreeNode treeNode2 = new TreeNode((TreeBean) compParameter.componentBean, treeNode, sc);
				treeNode2.setText(sc.getText());
				if (treeNode == null) {
					treeNode2.setJsDblclickCallback("return false;");
				}
				nodes.add(treeNode2);
			}
			return nodes;
		} else {
			return null;
		}
	}

	public IQueryEntitySet<SchoolCatalog> queryCatalogs(final PageRequestResponse requestResponse, final ITreeBeanAware parent) {
		final ITableEntityManager catalog_mgr = DataObjectManagerUtils.getTableEntityManager(ItSiteUtil.applicationModule, SchoolCatalog.class);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		if (parent == null) {
			sql.append(Table.nullExpr(catalog_mgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(parent.getId());
		}
		sql.append(" order by oorder");
		return catalog_mgr.query(new ExpressionValue(sql.toString(), al.toArray()), SchoolCatalog.class);
	}
}
