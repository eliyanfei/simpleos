package net.simpleframework.organization.component.register;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.web.page.component.ui.validatecode.DefaultValidateCodeHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserRegisterAction extends AbstractAjaxRequestHandle {
	public IForward checked(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = UserRegisterUtils
				.getComponentParameter(compParameter);
		final IUserRegisterHandle rHandle = (IUserRegisterHandle) nComponentParameter
				.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String user_account = nComponentParameter.getRequestParameter("user_account");
				if (StringUtils.hasText(user_account)) {
					if (rHandle.checked(compParameter, user_account)) {
						json.put("result", LocaleI18n.getMessage("UserRegisterAction.0", user_account));
					} else {
						json.put("result", LocaleI18n.getMessage("UserRegisterAction.1", user_account));
					}
				} else {
					json.put("result", LocaleI18n.getMessage("UserRegisterAction.2"));
				}
			}
		});
	}

	public IForward regist(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = UserRegisterUtils
				.getComponentParameter(compParameter);
		final IUserRegisterHandle rHandle = (IUserRegisterHandle) nComponentParameter
				.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (!DefaultValidateCodeHandle.isValidateCode(compParameter.request,
						"textRegistValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final String user_account = nComponentParameter.getRequestParameter("user_account");
					final IAccount account = OrgUtils.am().getAccountByName(user_account);
					if (account != null) {
						json.put("account", LocaleI18n.getMessage("UserRegisterAction.0", user_account));
					} else {
						final IUser user = OrgUtils.um().getUserByEmail(nComponentParameter.getRequestParameter("user_email"));
						if (user != null) {
							json.put("email", "该邮箱已经存在！");
							return;
						}
						final Map<String, Object> data = new HashMap<String, Object>();
						data.put("user_account", user_account);
						putParameter(nComponentParameter, data, "user_password");
						data.put("text", nComponentParameter.getRequestParameter("user_text"));
						data.put("email", nComponentParameter.getRequestParameter("user_email"));
						final String mobile = nComponentParameter.getRequestParameter("user_mobile");
						if (StringUtils.hasText(mobile)) {
							data.put("mobile", mobile);
						}
						data.put("sex", nComponentParameter.getRequestParameter("user_sex"));
						data.put("birthday", ConvertUtils.toDate(
								nComponentParameter.getRequestParameter("user_birthday"),
								IUser.birthdayDateFormat));
						rHandle.regist(nComponentParameter, data);
						final String jsCallback = rHandle.getJavascriptCallback(nComponentParameter,
								"submit", account);
						if (StringUtils.hasText(jsCallback)) {
							json.put("jsCallback", jsCallback);
						}
					}
				}
			}
		});
	}
}
