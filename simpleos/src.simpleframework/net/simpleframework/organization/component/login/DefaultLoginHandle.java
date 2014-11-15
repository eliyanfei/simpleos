package net.simpleframework.organization.component.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.a.ItSiteUtil;
import net.itsite.ItSiteCache;
import net.itsite.ItSiteOrganizationApplicationModule.AccountExt;
import net.itsite.permission.PlatformUtis;
import net.itsite.utils.MD5;
import net.itsite.utils.UUIDHexGenerator;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.core.ApplicationModuleException;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrganizationException;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.EAccountStatus;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.account.LoginObject;
import net.simpleframework.organization.account.LoginObject.EAccountType;
import net.simpleframework.organization.component.register.DefaultUserRegisterHandle;
import net.simpleframework.organization.component.register.UserRegisterUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.LastUrlFilterListener;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.SkinUtils;
import net.simpleframework.web.page.component.AbstractComponentHandle;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultLoginHandle extends AbstractComponentHandle implements ILoginHandle {
	//TODO 修改密码
	@Override
	public Map<String, Object> login(final ComponentParameter compParameter) {
		final LoginBean loginBean = (LoginBean) compParameter.componentBean;
		final HttpServletRequest request = compParameter.request;
		final String loginCallback = loginBean.getLoginCallback();
		final Map<String, Object> json = new HashMap<String, Object>();

		if (StringUtils.hasText(loginCallback)) {
			json.put("callback", loginCallback);
		}
		final HttpSession httpSession = request.getSession();
		final String accountName = request.getParameter("_accountName");
		IAccount accountObject = AccountSession.getLogin(httpSession);
		if (accountObject != null) {
			final IUser user = accountObject.user();
			if (user != null && user.getName().equals(accountName)) {
				json.put("ok", Boolean.TRUE);
				json.put("account", LocaleI18n.getMessage("DefaultLoginHandle.2"));
				return json;
			}
		}

		EAccountType accountType = null;
		try {
			accountType = EAccountType.valueOf(request.getParameter("_accountType"));
		} catch (final Exception e) {
		}
		final LoginObject loginObject = new LoginObject(accountName, accountType);
		final String passwordName = request.getParameter("_passwordName");
		beforeLogin(compParameter, loginObject, passwordName);

		
		try {
			accountObject = loginObject.getAccount();
			if (accountObject != null) {
				if (!accountObject.getPassword().equals(MD5.getHashString(passwordName))) {
					json.put("password", LocaleI18n.getMessage("DefaultLoginHandle.0"));
				} else {
					final EAccountStatus status = accountObject.getStatus();
					if (status == EAccountStatus.normal) {
						afterLogin(compParameter, loginObject);
						final String lastUrl = (String) httpSession.getAttribute(LastUrlFilterListener.SESSION_LAST_URL);
						if (StringUtils.hasText(lastUrl)) {
							json.put("url", lastUrl);
							httpSession.removeAttribute(LastUrlFilterListener.SESSION_LAST_URL);
						} else {
							json.put("url", compParameter.wrapContextPath(loginBean.getLoginForward()));
						}
						json.put("ok", Boolean.TRUE);
					} else if (status == EAccountStatus.locked) {
						json.put("status", LocaleI18n.getMessage("DefaultLoginHandle.3"));
					} else if (status == EAccountStatus.register) {
						json.put("status", LocaleI18n.getMessage("DefaultLoginHandle.4", "<a accountId=\"" + accountObject.getId() + "\">", "</a>"));
					}
				}
			} else {
				json.put("account", LocaleI18n.getMessage("DefaultLoginHandle.1"));
			}
		} catch (final OrganizationException e) {
			json.put("account", e.getMessage());
		}
		return json;
	}

	@Override
	public void beforeLogin(final ComponentParameter compParameter, final LoginObject loginObject, final String password) {
	}

	@Override
	public void afterLogin(final ComponentParameter compParameter, final LoginObject loginObject) {
		AccountSession.setLogin(compParameter.request, loginObject);
		final AccountExt accountExt = (AccountExt) loginObject.getAccount();
		if (accountExt != null) {
			PlatformUtis.getPaltformMenusByUser(accountExt.user(), true);
			SkinUtils.setSessionSkin(compParameter.getSession(), accountExt.getSkin());
		}
	}

	@Override
	public void mailGetPassword(final IUser user) {
		final MailMessageNotification mailMessage = new MailMessageNotification();
		mailMessage.setHtmlContent(true);
		mailMessage.getTo().add(user);
		mailMessage.setSubject(LocaleI18n.getMessage("LoginAction.2", PageUtils.pageContext.getApplication().getApplicationConfig().getTitle()));

		final Map<String, Object> variable = new HashMap<String, Object>();
		variable.put("usertext", user);
		variable.put("username", user.getName());
		final String id = UUIDHexGenerator.generator();
		variable.put("url", ItSiteUtil.url + "/findpass.html?sid=" + id);
		try {
			mailMessage.setTextBody(ScriptEvalUtils.replaceExpr(
					variable,
					IoUtils.getStringFromInputStream(DefaultLoginHandle.class.getClassLoader().getResourceAsStream(
							"net/simpleframework/organization/component/login/get_pwd.html"))));
			NotificationUtils.sendMessage(mailMessage);
			ItSiteCache.findPassword(id, user.getEmail());
		} catch (IOException e) {
			throw ApplicationModuleException.wrapException(e);
		}
	}

	@Override
	public void mailRegistActivation(final IAccount account) {
		UserRegisterUtils.sentMailActivation(account, DefaultUserRegisterHandle.class, "account_active.html");
	}
}
