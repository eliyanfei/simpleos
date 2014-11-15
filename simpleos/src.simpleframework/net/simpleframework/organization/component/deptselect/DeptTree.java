package net.simpleframework.organization.component.deptselect;

import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DeptTree extends AbstractTreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final AbstractTreeBean treeBean = (AbstractTreeBean) compParameter.componentBean;
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				(DeptSelectBean) treeBean.getAttribute("__deptSelect"));
		return ((IDeptSelectHandle) nComponentParameter.getComponentHandle()).getTreenodes(
				nComponentParameter, treeBean, treeNode);
	}
}
