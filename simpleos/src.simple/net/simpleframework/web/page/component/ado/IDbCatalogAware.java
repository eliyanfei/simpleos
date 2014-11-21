package net.simpleframework.web.page.component.ado;

import java.util.Collection;

import net.simpleframework.core.bean.ITextBeanAware;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IDbCatalogAware {
	String getCatalogIdName(PageRequestResponse requestResponse);

	Object getCatalogId(ComponentParameter compParameter);

	ITextBeanAware getCatalogById(ComponentParameter compParameter, Object id);

	Collection<? extends ITreeBeanAware> getSelectedCatalogs(ComponentParameter compParameter,
			ITreeBeanAware parent);

	Collection<? extends ITreeBeanAware> getMove2Catalogs(ComponentParameter compParameter,
			ITreeBeanAware parent);
}
