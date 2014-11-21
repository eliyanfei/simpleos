package net.simpleframework.sysmgr.dict;

import net.simpleframework.content.component.catalog.Catalog;
import net.simpleframework.content.component.catalog.DefaultCatalogHandle;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractDictHandle extends DefaultCatalogHandle {
	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return SysDict.class;
	}

	@Override
	public IApplicationModule getApplicationModule() {
		return DictUtils.applicationModule;
	}

	@Override
	public IDataObjectQuery<? extends Catalog> catalogs(final ComponentParameter compParameter,
			final Object parentId) {
		return beans(compParameter, parentId, "order by oorder asc");
	}

	@Override
	protected boolean hideOwnerMgrMenu() {
		return true;
	}
}
