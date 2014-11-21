package net.simpleframework.sysmgr.dict.component.dictselect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.sysmgr.dict.SysDict;
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
public class DictTree extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;

		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter, (DictSelectBean) treeBean.getAttribute("__dictSelect"));

		final IDictSelectHandle dHandle = (IDictSelectHandle) nComponentParameter.getComponentHandle();
		SysDict parent = null;
		if (treeNode != null) {
			parent = (SysDict) treeNode.getDataObject();
		}

		final Collection<SysDict> dictItems = dHandle.getDictItems(nComponentParameter, parent);
		if (dictItems != null) {
			final ArrayList<AbstractTreeNode> al = new ArrayList<AbstractTreeNode>();
			for (final SysDict dictItem : dictItems) {
				final TreeNode tn = new TreeNode(treeBean, treeNode, dictItem);
				tn.setId(dictItem.getName());
				if (dictItem.getMark() == SysDict.MARK_UNSELECTED) {
					tn.setJsDblclickCallback("Prototype.emptyFunction();");
				}
				al.add(tn);
			}
			return al;
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Object> getTreenodeAttributes(final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				(DictSelectBean) compParameter.componentBean.getAttribute("__dictSelect"));
		final IDictSelectHandle dHandle = (IDictSelectHandle) nComponentParameter.getComponentHandle();
		return dHandle.getDictItemAttributes(nComponentParameter, (SysDict) treeNode.getDataObject());
	}
}
