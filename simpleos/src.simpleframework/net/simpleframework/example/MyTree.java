package net.simpleframework.example;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

public class MyTree extends AbstractTreeHandle {

	@Override
	public boolean drop(final ComponentParameter compParameter, final AbstractTreeNode drag,
			final AbstractTreeNode drop) {
		return true;
	}
}
