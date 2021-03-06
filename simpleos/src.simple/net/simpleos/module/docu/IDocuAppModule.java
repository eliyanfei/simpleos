package net.simpleos.module.docu;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.bean.ITreeBeanAware;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleos.i.ISimpleosApplicationModule;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午4:18:10 
 *
 */
public interface IDocuAppModule extends ISimpleosApplicationModule {

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
