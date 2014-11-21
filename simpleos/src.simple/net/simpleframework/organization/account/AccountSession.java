package net.simpleframework.organization.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.itsite.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.LoginObject.EAccountType;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.LastUrlFilterListener;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.PageEventAdapter;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.UrlForward;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AccountSession extends ALoggerAware {
	private final static String LOGIN_ACCOUNT = "__login_account";

	private final static List<LoginObject> accounts = new ArrayList<LoginObject>();

	public static void logout(IAccount account) {
		try {
			LoginObject rlo = null;
			for (LoginObject lo : accounts) {
				try {
					if ((lo == null) || (!lo.getAccount().equals2(account)))
						continue;
					rlo = lo;
				} catch (Exception localException) {
				}
			}

			if (rlo != null)
				accounts.remove(rlo);
		} catch (Exception localException1) {
		}
	}

	public static void init(final ServletContext servletContext) {
		final PageEventAdapter adapter = PageEventAdapter.getInstance(servletContext);
		adapter.addListener(new HttpSessionListener() {
			@Override
			public void sessionDestroyed(final HttpSessionEvent event) {
				final HttpSession httpSession = event.getSession();
				_logout(getLogin(httpSession), Math.max(httpSession.getCreationTime(), httpSession.getLastAccessedTime()));
			}

			@Override
			public void sessionCreated(final HttpSessionEvent event) {
			}
		});
		adapter.addListener(new ServletContextListener() {
			@Override
			public void contextDestroyed(final ServletContextEvent event) {
				final long l = System.currentTimeMillis();
				while (accounts.size() > 0) {
					_logout(accounts.remove(0).getAccount(), l);
				}
			}

			@Override
			public void contextInitialized(final ServletContextEvent event) {
			}
		});
	}

	public static boolean isLogin(final HttpSession httpSession) {
		return getLogin(httpSession) != null;
	}

	public static boolean isAccount(final HttpSession httpSession, final Object accountId) {
		final IAccount account = getLogin(httpSession);
		if (account != null) {
			final ID id = account.getId();
			return accountId instanceof ID ? id.equals(accountId) : id.getValue().equals(accountId);
		}
		return false;
	}

	public static LoginObject getLoginObject(final HttpSession httpSession) {
		final LoginObject loginObject = (LoginObject) httpSession.getAttribute(LOGIN_ACCOUNT);
		if (loginObject != null) {
			return loginObject;
		}
		return new LoginObject("", EAccountType.email);
	}

	public static IAccount getLogin(final HttpSession httpSession) {
		final LoginObject loginObject = (LoginObject) httpSession.getAttribute(LOGIN_ACCOUNT);
		try {
			if (loginObject != null) {
				final IAccount account = loginObject.getAccount();
				if (account != null && account.isLogin() && account.user() != null) {
					return account;
				}
			}
		} catch (final Exception e) {
		}
		return null;
	}

	public static void setLogin(final HttpServletRequest request, final LoginObject loginObject) {
		if (loginObject == null) {
			return;
		}
		String skin = ItSiteUtil.attrMap.get("sys.sys_skin");
		if (StringsUtils.isNotBlank1(skin)) {
			String[] ss = skin.split(",");
			if (ss.length == 1) {
				ItSiteUtil.setSkin(ss[0], loginObject.getAccount(), request);
			}
		}
		String language = ItSiteUtil.attrMap.get("sys.sys_language");
		if (StringsUtils.isNotBlank1(language)) {
			String[] ls = language.split(",");
			if (ls.length == 1) {
				ItSiteUtil.setLanguage(ls[0]);
			}
		}
		final IAccount account = loginObject.getAccount();
		if (account == null) {
			return;
		}

		account.setLastLoginIP(HTTPUtils.getRemoteAddr(request));
		account.setLastLoginDate(new Date());
		account.setLoginTimes(account.getLoginTimes() + 1);
		account.setLogin(true);

		OrgUtils.am().update(c2, account);

		/* 更新帐号信息 */
		AccountContext.update(account, "org_login", ID.zero);

		request.getSession().setAttribute(LOGIN_ACCOUNT, loginObject);

		accounts.add(loginObject);
	}

	public static void logout(final PageRequestResponse requestResponse) {
		final HttpSession httpSession = requestResponse.getSession();
		_logout(getLogin(httpSession), System.currentTimeMillis());
		httpSession.removeAttribute(LOGIN_ACCOUNT);
		httpSession.removeAttribute(LastUrlFilterListener.SESSION_LAST_URL);
		HTTPUtils.addCookie(requestResponse.response, "_account_pwd", null);
	}

	private static void _logout(final IAccount account, final long lastAccessedTime) {
		if (account == null) {
			return;
		}
		final long millis = Math.min(Math.max(lastAccessedTime - account.getLastLoginDate().getTime(), 0), 1000 * 60 * 30);
		account.setOnlineMillis(account.getOnlineMillis() + millis);
		account.setLogin(false);
		account.setLastLoginDate(new Date());
		OrgUtils.am().update(c1, account);
	}

	private static String[] c1 = new String[] { "onlineMillis", "login", "lastLoginDate" };

	private static String[] c2 = new String[] { "lastLoginIP", "lastLoginDate", "loginTimes", "login" };

	public static interface IJobProperty {

		String getJobProperty();
	}

	public static String getLoginRedirectUrl(final PageRequestResponse requestResponse, final IJobProperty jobProperty) {
		if (OrgUtils.deployPath == null) {
			return null;
		}
		final String job = jobProperty != null ? jobProperty.getJobProperty() : null;
		final HttpServletRequest httpRequest = requestResponse.request;
		final String durl = HTTPUtils.getRequestURI(requestResponse.request);
		if (durl.contains("manager") && (!OrgUtils.isMember(job, requestResponse.getSession()) || !ItSiteUtil.isManage(requestResponse))) {
			return "/index.html";
		}
		if (isLogin(requestResponse.getSession())) {
			return null;
		}
		final String account = HTTPUtils.getCookie(httpRequest, "_account_name");
		final String pwd = HTTPUtils.getCookie(httpRequest, "_account_pwd");
		if (StringUtils.hasText(account) && StringUtils.hasText(pwd)) {
			final EAccountType accountType = ConvertUtils.toEnum(EAccountType.class, HTTPUtils.getCookie(httpRequest, "_account_type"));
			setLogin(httpRequest, new LoginObject(StringUtils.decodeHexString(account), accountType));
			if (isLogin(requestResponse.getSession())) {
				return null;
			}
		}
		String url = httpRequest.getRequestURI();
		if ((!StringUtils.hasText(job) || IJob.sj_anonymous.equals(job)) && !url.endsWith("location.jsp")) {
			return null;
		}
		if (url.contains(OrgUtils.deployPath + "jsp/login_redirect")) {
			return null;
		}
		if (url.contains("login") && !url.endsWith("location.jsp")) {
			return null;
		}
		if (requestResponse.isHttpRequest()) {
			return OrgUtils.deployPath + "jsp/login_redirect_template.jsp?login_redirect="
					+ requestResponse.wrapContextPath(OrgUtils.applicationModule.getLoginUrl(requestResponse));
		} else {
			return OrgUtils.deployPath + "jsp/login_win_redirect.jsp?login_redirect="
					+ requestResponse.wrapContextPath(OrgUtils.applicationModule.getLoginUrl(requestResponse));
		}
	}

	public static IForward accessForward(final PageRequestResponse requestResponse, final String job, final String componentName) {
		if (OrgUtils.deployPath == null) {
			return null;
		}
		final String redirectUrl = getLoginRedirectUrl(requestResponse, new IJobProperty() {
			@Override
			public String getJobProperty() {
				return job;
			}
		});
		if (StringUtils.hasText(redirectUrl)) {
			return new UrlForward(redirectUrl);
		} else {
			if (StringUtils.hasText(job)) {
				if (!OrgUtils.isMember(job, requestResponse.getSession())) {
					return new UrlForward(OrgUtils.deployPath + "jsp/job_ajax_access.jsp?v=" + componentName + "&job=" + job);
				}
			}
		}
		return null;
	}
}
