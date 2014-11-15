package net.simpleframework.content.component.catalog;

import java.util.Collection;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeBean;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeHandle;
import net.simpleframework.web.page.component.ui.tree.AbstractTreeNode;
import net.simpleframework.web.page.component.ui.tree.TreeBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CatalogTree extends AbstractTreeHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		if (nComponentParameter.componentBean != null) {
			if ("selector".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("selector");
			} else if ("dynamicLoading".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("dynamicTree");
			} else if ("jsLoadedCallback".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("jsLoadedCallback");
			} else if ("cookies".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("cookies");
			} else if ("jobDrop".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("jobEdit");
			}
			if (!isDictionary((TreeBean) compParameter.componentBean)) {
				if ("name".equals(beanProperty)) {
					return "__catalogTree_" + nComponentParameter.componentBean.hashId();
				} else if ("containerId".equals(beanProperty)) {
					return "catalog_" + nComponentParameter.componentBean.hashId();
				}
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Collection<? extends AbstractTreeNode> getTreenodes(
			final ComponentParameter compParameter, final AbstractTreeNode treeNode) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		return ((ICatalogHandle) nComponentParameter.getComponentHandle()).getCatalogTreenodes(
				nComponentParameter, (AbstractTreeBean) compParameter.componentBean, treeNode,
				isDictionary((TreeBean) compParameter.componentBean));
	}

	private boolean isDictionary(final TreeBean treeBean) {
		return ConvertUtils.toBoolean(
				WebUtils.toQueryParams(treeBean.getParameters()).get("dictionary"), false);
	}

	@Override
	public boolean drop(final ComponentParameter compParameter, final AbstractTreeNode drag,
			final AbstractTreeNode drop) {
		final Catalog _drag = (Catalog) drag.getDataObject();
		final Catalog _drop = (Catalog) drop.getDataObject();
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(compParameter);
		final ICatalogHandle handle = (ICatalogHandle) nComponentParameter.getComponentHandle();
		final ITableEntityManager temgr = handle.getTableEntityManager(nComponentParameter);
		_drag.setParentId(_drop == null ? null : _drop.getId());
		temgr.update(_drag);
		return true;
	}
}
