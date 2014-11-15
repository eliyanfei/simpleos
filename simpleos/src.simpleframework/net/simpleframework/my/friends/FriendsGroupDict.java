package net.simpleframework.my.friends;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeBean;
import net.simpleframework.web.page.component.ui.tree.TreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FriendsGroupDict extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final IAccount login = AccountSession.getLogin(compParameter.getSession());
		if (login == null) {
			return null;
		}

		final Collection<TreeNode> coll = new ArrayList<TreeNode>();
		final ITableEntityManager fr_mgr = FriendsUtils.getTableEntityManager(FriendsGroup.class);
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		sql.append("userId=? and ");
		al.add(login.getId());
		if (treeNode == null) {
			sql.append(Table.nullExpr(fr_mgr.getTable(), "parentid"));
		} else {
			sql.append("parentid=?");
			al.add(((FriendsGroup) treeNode.getDataObject()).getId());
		}
		final IQueryEntitySet<FriendsGroup> qs = fr_mgr.query(
				new ExpressionValue(sql.toString(), al.toArray()), FriendsGroup.class);
		FriendsGroup fr;
		while ((fr = qs.next()) != null) {
			final TreeNode tn = new TreeNode((TreeBean) compParameter.componentBean, treeNode, fr);
			coll.add(tn);
		}
		return coll;
	}
}
