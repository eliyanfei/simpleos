package net.simpleframework.organization.component.register;

import java.util.Map;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultUserRegisterHandle extends AbstractComponentHandle implements IUserRegisterHandle {
	@Override
	public IAccount regist(final ComponentParameter compParameter, final Map<String, Object> data) {
		final String user_account = (String) data.remove("user_account");
		final String user_password = (String) data.remove("user_password");
		final IAccount account = OrgUtils.am().insertAccount(user_account, user_password, HTTPUtils.getRemoteAddr(compParameter.request),
				new IAccount.InsertCallback() {
					@Override
					public void init(final IAccount account) {
						account.setStatus(EAccountStatus.register);
					}

					@Override
					public void insert(final IUser user) {
						setObjectFromMap(user, data);
					}
				});
		mailRegistActivation(account);
		return account;
	}

	@Override
	public boolean checked(final ComponentParameter compParameter, final String userAccount) {
		return OrgUtils.am().getAccountByName(userAccount) != null;
	}

	@Override
	public void mailRegistActivation(final IAccount account) {
		try {
			UserRegisterUtils.sentMailActivation(account, DefaultUserRegisterHandle.class, "account_active.html");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter, final String jsAction, final Object bean) {
		if ("submit".equals(jsAction)) {
			return "alert('" + LocaleI18n.getMessage("UserRegisterAction.4") + "');";
		}
		return null;
	}
}
