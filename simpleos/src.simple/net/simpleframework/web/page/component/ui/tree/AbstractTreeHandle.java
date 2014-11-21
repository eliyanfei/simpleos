package net.simpleframework.web.page.component.ui.tree;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractTreeHandle extends AbstractComponentHandle implements ITreeHandle {

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		return null;
	}

	@Override
	public Map<String, Object> getTreenodeAttributes(final ComponentParameter compParameter,
			final AbstractTreeNode treeNode) {
		return null;
	}

	@Override
	public boolean drop(final ComponentParameter compParameter, final AbstractTreeNode drag,
			final AbstractTreeNode drop) {
		return false;
	}
}
