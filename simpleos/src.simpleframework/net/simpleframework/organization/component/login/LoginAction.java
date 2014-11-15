package net.simpleframework.organization.component.login;

import java.io.IOException;
import java.util.Map;

import net.itsite.utils.MD5;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.JsonForward;
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
public class LoginAction extends AbstractAjaxRequestHandle {

	public IForward login(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = LoginUtils.getComponentParameter(compParameter);
		final ILoginHandle handle = (ILoginHandle) nComponentParameter.getComponentHandle();
		return new JsonForward(handle.login(nComponentParameter));
	}

	public IForward logout(final ComponentParameter compParameter) {
		AccountSession.logout(compParameter);
		return null;
	}

	public IForward getpwd(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = LoginUtils.getComponentParameter(compParameter);
		final ILoginHandle lHandle = (ILoginHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (!DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textGetpwdValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final String email = compParameter.getRequestParameter("textGetpwd");
					final IUser user = OrgUtils.um().query(new ExpressionValue("email=?", new Object[] { email })).next();
					if (user == null) {
						json.put("getpwd", LocaleI18n.getMessage("LoginAction.1", email));
					} else {
						lHandle.mailGetPassword(user);
						json.put("getpwd", LocaleI18n.getMessage("LoginAction.3"));
					}
				}
			}
		});
	}

	public IForward sendMail2(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = LoginUtils.getComponentParameter(compParameter);
		final ILoginHandle lHandle = (ILoginHandle) nComponentParameter.getComponentHandle();
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) throws IOException {
				final IAccount account = OrgUtils.am().queryForObjectById(compParameter.getRequestParameter("accountId"));
				lHandle.mailRegistActivation(account);
				json.put("result", LocaleI18n.getMessage("LoginAction.0"));
			}
		});
	}

	//TODO
	public IForward editmail(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				if (!DefaultValidateCodeHandle.isValidateCode(compParameter.request, "textEditmailValidateCode")) {
					json.put("validateCode", DefaultValidateCodeHandle.getErrorString());
				} else {
					final IAccount account = OrgUtils.am().queryForObjectById(compParameter.getRequestParameter("accountId"));
					if (!StringUtils.blank(account.getPassword()).equals(MD5.getHashString(compParameter.getRequestParameter("em_password")))) {
						json.put("password", LocaleI18n.getMessage("LoginAction.4"));
					} else {
						final IUser user = account.user();
						user.setEmail(compParameter.getRequestParameter("em_mail"));
						OrgUtils.um().update(user);
					}
				}
			}
		});
	}
}
