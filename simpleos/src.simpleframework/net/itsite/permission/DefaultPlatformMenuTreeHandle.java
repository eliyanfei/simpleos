package net.itsite.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.a.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

public class DefaultPlatformMenuTreeHandle extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final String jobId = compParameter.getRequestParameter("jobId");
		if (StringsUtils.isBlank(jobId)) {
			return null;
		}
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final List<PermissionBean> list = IDataObjectQuery.Utils.toList(ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule,
				PermissionBean.class).query(new ExpressionValue("job_id=?", new Object[] { jobId }), PermissionBean.class));
		final Collection<AbstractTreeNode> _treeNode = new ArrayList<AbstractTreeNode>();
		if (treeNode != null)
			return null;
		
		final TreeNode root = new TreeNode(treeBean, treeNode, "功能菜单");
		root.setOpened(true);
		_treeNode.add(root);
		
		for (final MenuItem item : ItSiteUtil.menuList) {
			if (item.getTitle().equals("-")) {
				continue;
			}
			final TreeNode rootTreeNode = new TreeNode(treeBean, treeNode, item);
			rootTreeNode.setText(item.getTitle());
			rootTreeNode.setCheck(hasMenu(list, item));
			root.getChildren().add(rootTreeNode);
			bindChildrenMenu(list, treeBean, rootTreeNode, item.getChildren());
		}
		return _treeNode;
	}

	public int hasMenu(final List<PermissionBean> list, final MenuItem item) {
		for (final PermissionBean bean : list) {
			if (bean.getMenu_name().equals(item.getTitle())) {
				return 1;
			}
		}
		return 0;
	}

	private void bindChildrenMenu(final List<PermissionBean> list, final AbstractTreeBean treeBean, final TreeNode treeNode,
			final Collection<MenuItem> children) {
		if (children == null || children.size() <= 0) {
			return;
		}
		final Iterator<MenuItem> menuIterator = children.iterator();
		while (menuIterator.hasNext()) {
			final MenuItem item = menuIterator.next();
			if (item.getTitle().equals("-")) {
				continue;
			}
			final TreeNode subTreeNode = new TreeNode(treeBean, treeNode, item);
			subTreeNode.setText(item.getTitle());
			final int check = hasMenu(list, item);
			subTreeNode.setCheck(check);
			if (check == 1) {
				treeNode.setCheck(check);
			}
			treeNode.getChildren().add(subTreeNode);
			bindChildrenMenu(list, treeBean, subTreeNode, item.getChildren());
		}
	}

}
