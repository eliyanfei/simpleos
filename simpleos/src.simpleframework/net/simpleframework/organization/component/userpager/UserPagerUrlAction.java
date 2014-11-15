package net.simpleframework.organization.component.userpager;

import net.simpleframework.web.page.AbstractUrlForward;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerAction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserPagerUrlAction extends AbstractDbTablePagerAction {
	@Override
	protected ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return UserPagerUtils.getComponentParameter(requestResponse);
	}

	public IForward addUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager, "/jsp/user_add.jsp");
	}

	public IForward editUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager, "/jsp/ue/user_edit.jsp");
	}

	public IForward accountStatUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager,
				"/jsp/ua/user_account_stat.jsp");
	}

	public IForward accountLogUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager,
				"/jsp/ua/user_account_log.jsp");
	}

	public IForward accountBindingUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager,
				"/jsp/ua/user_account_binding.jsp");
	}

	public IForward tabUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager,
				"/jsp/" + compParameter.getRequestParameter("tp"));
	}

	public IForward sentMailUrl(final ComponentParameter compParameter) {
		return AbstractUrlForward.componentUrl(UserPagerRegistry.userPager, "/jsp/sent_mail.jsp");
	}
}
