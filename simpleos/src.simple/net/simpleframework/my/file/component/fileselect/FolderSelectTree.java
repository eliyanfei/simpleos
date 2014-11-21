package net.simpleframework.my.file.component.fileselect;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.my.file.MyFileUtils;
import net.simpleframework.my.file.MyFolder;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
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
public class FolderSelectTree extends AbstractTreeHandle {
	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account == null) {
			return null;
		}
		final ArrayList<AbstractTreeNode> treenodes = new ArrayList<AbstractTreeNode>();
		final ITableEntityManager temgr = MyFileUtils.getTableEntityManager(MyFolder.class);
		ExpressionValue ev;
		if (treeNode == null) {
			ev = new ExpressionValue("userid=? and " + Table.nullExpr(temgr.getTable(), "parentid"),
					new Object[] { account.getId() });
		} else {
			ev = new ExpressionValue("userid=? and parentid=?", new Object[] { account.getId(),
					((MyFolder) treeNode.getDataObject()).getId() });
		}
		final IQueryEntitySet<MyFolder> qs = temgr.query(ev, MyFolder.class);
		MyFolder myFolder;
		while ((myFolder = qs.next()) != null) {
			final TreeNode treeNode2 = new TreeNode((AbstractTreeBean) compParameter.componentBean,
					treeNode, myFolder);
			treeNode2.setId(ConvertUtils.toString(myFolder.getId()));
			treeNode2.setPostfixText(WebUtils.enclose(myFolder.getFiles()));
			treenodes.add(treeNode2);
		}
		return treenodes;
	}
}
