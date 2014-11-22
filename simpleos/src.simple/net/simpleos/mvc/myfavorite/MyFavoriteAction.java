package net.simpleos.mvc.myfavorite;

import java.util.Map;

import net.simpleframework.core.id.LongID;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleos.SimpleosUtil;

import org.springframework.util.StringUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-16上午09:14:06
 */
public class MyFavoriteAction extends AbstractAjaxRequestHandle {
	/**
	 * 删除选择的消息
	 * @param compParameter
	 * @return
	 */
	public IForward cancelFavorite(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				final String id = compParameter.getParameter("id");
				final String refId = compParameter.getParameter("refId");
				final IAccount account = SimpleosUtil.getLoginAccount(compParameter);
				if (account == null)
					return;
				if (StringUtils.hasText(id)) {
					MyFavoriteUtils.cancelFavoriteById(id, account.getId());
				} else if (StringUtils.hasText(refId)) {
					MyFavoriteUtils.cancelFavorite(refId, account.getId());
				}
				json.put("refId", refId);
				json.put("id", id);
			}
		});
	}

	/**
	 * 添加选择的消息
	 * @param compParameter
	 * @return
	 */
	public IForward addFavorite(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {

			@Override
			public void doAction(Map<String, Object> json) throws Exception {
				try {
					String action = compParameter.getParameter("action");
					String title = compParameter.getParameter("title");
					String refId = compParameter.getParameter("refId");
					String refId1 = compParameter.getParameter("refId1");
					String type = compParameter.getParameter("type");
					MyFavoriteBean favoriteBean = new MyFavoriteBean();
					favoriteBean.setTitle(title);
					EFunctionModule module = EFunctionModule.valueOf(type);
					if (module == EFunctionModule.news) {
						favoriteBean.setUrl("/news/v/" + refId + ".html");
					} else if (module == EFunctionModule.blog) {
						favoriteBean.setUrl("/blog/v/" + refId + ".html");
					} else if (module == EFunctionModule.bbs) {
						favoriteBean.setUrl("/bbs/" + refId1 + "/" + refId + ".html");
					} else if (module == EFunctionModule.docu) {
						favoriteBean.setUrl("/docu/v/" + refId + ".html");
					}
					favoriteBean.setRefId(new LongID(refId));
					favoriteBean.setType(module);
					favoriteBean.setUserId(SimpleosUtil.getLoginAccount(compParameter).getId());
					MyFavoriteUtils.appModule.doUpdate(favoriteBean);
					json.put("action", action);
				} catch (Exception e) {
				}
			}
		});
	}
}
