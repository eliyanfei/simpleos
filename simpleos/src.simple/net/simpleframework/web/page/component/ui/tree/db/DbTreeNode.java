package net.simpleframework.web.page.component.ui.tree.db;

import java.util.Map;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DbTreeNode extends AbstractTreeNode {

	public DbTreeNode(final AbstractTreeBean treeBean, final DbTreeNode parent,
			final Map<String, Object> rowData) {
		super(treeBean, parent, rowData);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getRowData() {
		return (Map<String, Object>) getDataObject();
	}

	@Override
	public DbTreeBean getTreeBean() {
		return (DbTreeBean) super.getTreeBean();
	}

	@Override
	public String getId() {
		return ConvertUtils.toString(getRowData().get(getTreeBean().getIdName()));
	}

	@Override
	public String getText() {
		return ConvertUtils.toString(getRowData().get(getTreeBean().getTextName()));
	}
}
