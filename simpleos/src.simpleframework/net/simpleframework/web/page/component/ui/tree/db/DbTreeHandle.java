package net.simpleframework.web.page.component.ui.tree.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DbTreeHandle extends AbstractTreeHandle {

	@Override
	public Collection<AbstractTreeNode> getTreenodes(final ComponentParameter compParameter,
			final AbstractTreeNode treeNode) {
		final Collection<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>();
		final DbTreeBean dbTree = (DbTreeBean) compParameter.componentBean;
		final DbTreeNode dbNode = (DbTreeNode) treeNode;
		final IDataObjectQuery<Map<String, Object>> set = getDataQuery(compParameter, dbNode);
		Map<String, Object> rowData;
		while ((rowData = set.next()) != null) {
			nodes.add(new DbTreeNode(dbTree, dbNode, rowData));
		}
		return nodes;
	}

	protected IDataObjectQuery<Map<String, Object>> getDataQuery(
			final ComponentParameter compParameter, final DbTreeNode dbNode) {
		final DbTreeBean dbTree = (DbTreeBean) compParameter.componentBean;
		final String table = dbTree.getTableName();
		final String id = dbTree.getIdName();
		final ITableEntityManager mgr = DataObjectManagerUtils.getTableEntityManager(
				PageUtils.pageContext.getApplication(), new Table(table, id));
		final String parentId = dbTree.getParentIdName();
		if (dbNode == null) {
			return mgr.query(new ExpressionValue(getRootExpressionValue(parentId)));
		} else {
			return mgr.query(new ExpressionValue(parentId + "=?", new Object[] { dbNode.getRowData()
					.get(id) }));
		}
	}

	protected String getRootExpressionValue(final String parentId) {
		return parentId + "=0";
	}
}
