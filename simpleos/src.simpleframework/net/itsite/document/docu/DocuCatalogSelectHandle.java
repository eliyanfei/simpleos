package net.itsite.document.docu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

public class DocuCatalogSelectHandle extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		return getForumDictTree(compParameter, treeNode);
	}

	@Override
	public Map<String, Object> getTreenodeAttributes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("selected", treeNode.getParent() != null);
		return attributes;
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "t");
		putParameter(compParameter, parameters, "s");
		putParameter(compParameter, parameters, "c");
		return parameters;
	}

	public Collection<? extends AbstractTreeNode> getForumDictTree(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final ITableEntityManager temgr = DocuUtils.applicationModule.getDataObjectManager(DocuCatalog.class);
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		if (treeNode == null) {
			sql.append(Table.nullExpr(temgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(((DocuCatalog) treeNode.getDataObject()).getId());
		}

		final String imgBase = BbsUtils.getCssPath(compParameter) + "/images/";
		final boolean navTree = "catalogSelectTree".equals(treeBean.getName());
		final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
		final IQueryEntitySet<DocuCatalog> qs = temgr.query(new ExpressionValue(sql.toString() + " order by oorder desc", al.toArray()),
				DocuCatalog.class);
		DocuCatalog catalog;
		final String applicationUrl = DocuUtils.applicationModule.getApplicationUrl(compParameter);
		while ((catalog = qs.next()) != null) {
			final TreeNode node = new TreeNode(treeBean, treeNode, catalog);
			node.setAttribute("name", catalog.getName());
			if (navTree) {
				node.setOpened(true);
				if (node.getParent() != null) {
					final StringBuilder sb = new StringBuilder();
					sb.append("<a style=\"vertical-align: middle;\"");
					sb.append(" href=\""
							+ WebUtils.addParameters(compParameter.getServletContext().getContextPath() + applicationUrl,
									"m=" + treeNode.getAttribute("name") + "&mc=" + catalog.getId().getValue()) + "\"");
					sb.append("\">").append(catalog.getText()).append("</a>");
					node.setText(sb.toString());
				} else {
					node.setJsDblclickCallback("return false;");
				}
			}
			if (node.getParent() != null) {
				node.setImage(imgBase + "forum_link.png");
			} else {
				node.setImage(imgBase + "forum_c.png");
			}
			nodes.add(node);
		}
		return nodes;
	}
}
