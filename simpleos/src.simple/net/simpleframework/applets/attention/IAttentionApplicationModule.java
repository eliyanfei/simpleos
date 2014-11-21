package net.simpleframework.applets.attention;

import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.core.id.ID;
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
public interface IAttentionApplicationModule extends IWebApplicationModule {

	AttentionBean get(PageRequestResponse requestResponse, EFunctionModule vtype, Object attentionId);

	AttentionBean insert(PageRequestResponse requestResponse, EFunctionModule vtype, ID attentionId);

	AttentionBean insert(ID userId, EFunctionModule vtype, ID attentionId);

	IQueryEntitySet<AttentionBean> queryAttentions(EFunctionModule vtype, ID attentionId);

	void delete(PageRequestResponse requestResponse, EFunctionModule vtype, Object attentionId);

	void deleteByAttentionId(PageRequestResponse requestResponse, EFunctionModule vtype, Object[] attentionIds);

	void sentMessage(PageRequestResponse requestResponse, EFunctionModule vtype, ISentCallback callback);
}
