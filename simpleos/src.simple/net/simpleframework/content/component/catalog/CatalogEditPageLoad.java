package net.simpleframework.content.component.catalog;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CatalogEditPageLoad extends DefaultPageHandle {

	public void catalogPageLoaded(final PageParameter pageParameter,
			final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = CatalogUtils
				.getComponentParameter(pageParameter);
		final ICatalogHandle cHandle = (ICatalogHandle) nComponentParameter.getComponentHandle();
		final Catalog catalog = cHandle.getEntityBeanByRequest(nComponentParameter);
		Object parentId = null;
		if (catalog != null) {
			cHandle.doLoadPropEditor(pageParameter, catalog, dataBinding);
			parentId = catalog.getParentId();
		} else {
			parentId = pageParameter.getRequestParameter("parentId");
		}
		final Catalog parentItem = cHandle.getEntityBeanById(nComponentParameter, parentId);
		if (parentItem != null) {
			dataBinding.put("catalog_parentId", parentItem.getId().getValue());
			dataBinding.put("catalog_parentText", parentItem.getText());
		}
	}
}
