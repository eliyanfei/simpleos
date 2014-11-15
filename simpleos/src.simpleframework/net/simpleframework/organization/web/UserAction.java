package net.simpleframework.organization.web;

import java.util.Map;

import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserAction extends AbstractAjaxRequestHandle {

	public IForward doLogout(final ComponentParameter compParameter) {
		final IAccount account = OrgUtils.am().queryForObjectById(
				compParameter.getRequestParameter("userId"));
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws Exception {
				if (account != null) {
					account.setLogin(false);
					OrgUtils.am().update(new String[] { "login" }, account);
					json.put("result", Boolean.TRUE);
				}
			}
		});
	}
}
