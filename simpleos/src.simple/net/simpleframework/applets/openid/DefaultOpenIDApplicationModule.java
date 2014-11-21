package net.simpleframework.applets.openid;

import javax.servlet.http.HttpServletRequestWrapper;

import net.simpleframework.core.IInitializer;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount.InsertCallback;
import net.simpleframework.organization.account.LoginObject;
import net.simpleframework.organization.account.LoginObject.EAccountType;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.LastUrlFilterListener;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;

import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultOpenIDApplicationModule extends AbstractWebApplicationModule implements
		IOpenIDApplicationModule {
	static final String deployName = "applets/openid";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(OpenIDUtils.class, deployName);
	}

	private OpenIdManager openIdManager;

	private synchronized OpenIdManager getOpenIdManager(final PageRequestResponse requestResponse) {
		if (openIdManager == null) {
			openIdManager = new OpenIdManager();
			openIdManager.setReturnTo(getReturnToURL(requestResponse));
		}
		return openIdManager;
	}

	protected String getReturnToURL(final PageRequestResponse requestResponse) {
		return getApplication().getApplicationConfig().getServerUrl();
	}

	private static final String ATTR_MAC = "attr_mac";

	private static final String ATTR_ALIAS = "attr_alias";

	@Override
	public String getOpUrl(final PageRequestResponse requestResponse) {
		final OpenIdManager manager = getOpenIdManager(requestResponse);
		final String op = requestResponse.getRequestParameter("op");
		if ("Google".equals(op) || "Yahoo".equals(op)) {
			final Endpoint endpoint = manager.lookupEndpoint(op);
			final Association association = manager.lookupAssociation(endpoint);
			final String url = manager.getAuthenticationUrl(endpoint, association);
			requestResponse.setSessionAttribute(ATTR_MAC, association.getRawMacKey());
			requestResponse.setSessionAttribute(ATTR_ALIAS, endpoint.getAlias());
			return url;
		} else {
			return null;
		}
	}

	@Override
	public OpenIDBean getOpenIDBean(final PageRequestResponse requestResponse) {
		final OpenIdManager manager = getOpenIdManager(requestResponse);
		try {
			final Authentication authentication = manager.getAuthentication(
					new HttpServletRequestWrapper(requestResponse.request) {
						@Override
						public String getParameter(final String name) {
							return WebUtils.toLocaleString(super.getParameter(name));
						}
					}, (byte[]) requestResponse.getSessionAttribute(ATTR_MAC),
					(String) requestResponse.getSessionAttribute(ATTR_ALIAS));
			final OpenIDBean authentication2 = new OpenIDBean();
			authentication2.email = authentication.getEmail();
			authentication2.text = authentication.getFullname();
			return authentication2;
		} catch (final Exception e) {
			return null;
		}
	}

	@Override
	public String login(final PageRequestResponse requestResponse, final OpenIDBean openIDBean) {
		if (openIDBean == null) {
			return null;
		}
		final String email = openIDBean.getEmail();
		IUser user = OrgUtils.um().getUserByEmail(email);
		if (user == null) {
			final String name = openIDBean.getName();
			final String text = openIDBean.getText();
			final String password = openIDBean.getPassword();
			if (StringUtils.hasText(name) && StringUtils.hasText(text)
					&& StringUtils.hasText(password)) {
				user = OrgUtils
						.am()
						.insertAccount(name, password, HTTPUtils.getRemoteAddr(requestResponse.request),
								new InsertCallback() {
									@Override
									public void insert(final IUser user) {
										user.setText(text);
										user.setEmail(email);
									}
								}).user();
			}
		}
		if (user != null && user.account() != null) {
			AccountSession.setLogin(requestResponse.request, new LoginObject(user.getName(),
					EAccountType.normal));
			if (AccountSession.isLogin(requestResponse.getSession())) {
				return StringUtils.text((String) requestResponse
						.getSessionAttribute(LastUrlFilterListener.SESSION_LAST_URL), "/");
			}
		}
		return null;
	}
}
