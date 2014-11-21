package net.simpleframework.content.component.newspager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
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
public class NewsCatalogSelect extends AbstractTreeHandle {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, NewsPagerUtils.BEAN_ID);
		return parameters;
	}

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final ComponentParameter nComponentParameter = NewsPagerUtils
				.getComponentParameter(compParameter);
		final INewsPagerHandle npHandle = (INewsPagerHandle) nComponentParameter.getComponentHandle();
		if (npHandle == null) {
			return null;
		}
		Collection<? extends IIdBeanAware> catalogs = null;
		if (treeNode == null) {
			catalogs = npHandle.listNewsCatalog(nComponentParameter);
		} else {
			if (!(treeNode.getDataObject() instanceof ITreeBeanAware)) {
				return null;
			}
		}
		if (catalogs == null) {
			catalogs = npHandle.getSelectedCatalogs(nComponentParameter, treeNode == null ? null
					: (ITreeBeanAware) treeNode.getDataObject());
		}
		if (catalogs != null) {
			final Collection<TreeNode> nodes = new ArrayList<TreeNode>();
			for (final IIdBeanAware catalog : catalogs) {
				final TreeNode treeNode2 = new TreeNode((TreeBean) compParameter.componentBean,
						treeNode, catalog);
				nodes.add(treeNode2);
			}
			return nodes;
		} else {
			return null;
		}
	}
}
