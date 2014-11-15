package net.simpleframework.example;

import java.util.Collection;

import net.simpleframework.content.component.catalog.DefaultCatalogHandle;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;

public class MyCatalogHandle extends DefaultCatalogHandle {
	@Override
	public void handleCreated(final PageRequestResponse requestResponse,
			final AbstractComponentBean componentBean) {
		PageUtils.doDatabase(DefaultCatalogHandle.class, componentBean);
	}

	@Override
	public Collection<? extends AbstractTreeNode> getCatalogTreenodes(
			final ComponentParameter compParameter, final AbstractTreeBean treeBean,
			final AbstractTreeNode treeNode, final boolean dictionary) {
		final Collection<? extends AbstractTreeNode> coll = super.getCatalogTreenodes(compParameter,
				treeBean, treeNode, dictionary);
		for (final AbstractTreeNode treeNode2 : coll) {
			treeNode2.setJsClickCallback("__file_list(\"" + treeNode2.getId() + "\");");
		}
		return coll;
	}
}
