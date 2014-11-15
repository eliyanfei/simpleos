package net.simpleframework.my.space;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IUser;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ISpaceApplicationModule extends IWebApplicationModule {

	String getSpaceUrl(PageRequestResponse requestResponse, IUser user);

	String getMySpaceNavigationHtml(PageRequestResponse requestResponse);

	String getUserNavigationPath(PageRequestResponse requestResponse);

	String tabs(PageRequestResponse requestResponse);

	String attentionTabs(PageRequestResponse requestResponse);

	String logTabs(PageRequestResponse requestResponse);

	// do method

	<T extends IDataObjectBean> T getBeanById(Object id, Class<T> beanClazz);

	SapceLogBean addSapceLog(PageRequestResponse requestResponse, String content, EFunctionModule refModule, ID refId, boolean transaction);

	int deleteSapceLog(PageRequestResponse requestResponse, Object id);

	IQueryEntitySet<SapceRemarkBean> remarkList(SapceLogBean sapceLog);

	SapceRemarkBean addSapceLogRemark(PageRequestResponse requestResponse, SapceLogBean sapceLog, String content, boolean opt1);

	int deleteSapceLogRemark(PageRequestResponse requestResponse, Object remarkid);

	IQueryEntitySet<SapceResourceBean> resourceList(SapceLogBean sapceLog);

	UserAttentionBean getUserAttentionById(PageRequestResponse requestResponse, Object attentionId);

	void addUserAttention(PageRequestResponse requestResponse, Object attentionId);

	void deleteUserAttention(PageRequestResponse requestResponse, Object attentionId);
}
