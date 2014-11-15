package net.itsite.document.docu;

import net.itsite.i.IItSiteApplicationModule;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.web.page.PageRequestResponse;

public interface IDocuApplicationModule extends IItSiteApplicationModule {

	/**
	* 增加阅读量
	* 
	* @param requestResponse
	* @return
	*/
	DocuBean getViewDocuBean(PageRequestResponse requestResponse);

	/**
	 * 获得相应的分类目录
	 */
	IQueryEntitySet<DocuCatalog> queryCatalogs(final PageRequestResponse requestResponse, ITreeBeanAware parent);

	/**
	* 获得相应的分类目录
	*/
	IQueryEntitySet<DocuCatalog> queryCatalogs(final Object catalogId);
}
