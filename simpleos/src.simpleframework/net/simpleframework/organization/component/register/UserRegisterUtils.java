package net.simpleframework.organization.component.register;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.a.ItSiteUtil;
import net.simpleframework.applets.notification.MailMessageNotification;
import net.simpleframework.applets.notification.NotificationUtils;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.script.ScriptEvalUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class UserRegisterUtils {
	public static final String BEAN_ID = "regist_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static void sentMailActivation(final IAccount account, final Class<?> c, final String filename) {
		final MailMessageNotification mailMessage = new MailMessageNotification();
		mailMessage.setHtmlContent(true);
		final IUser user = account.user();
		if (user == null) {
			return;
		}
		mailMessage.getTo().add(user);
		mailMessage.setSubject(LocaleI18n.getMessage("UserRegisterAction.3", OrgUtils.applicationModule.getApplication().getApplicationConfig()
				.getTitle()));
		final Map<String, Object> variable = new HashMap<String, Object>();
		final String serverUrl = ItSiteUtil.url + "/";
		final String url = serverUrl + getHomePath() + "/jsp/regist_activation.jsp?code="
				+ StringUtils.hash(ConvertUtils.toDateString(account.getCreateDate())) + "&accountId=" + account.getId();
		variable.put("serverUrl", serverUrl);
		variable.put("url", url);
		variable.put("account", user.getName());
		variable.put("password", account.getPassword());
		try {
			mailMessage.setTextBody(ScriptEvalUtils.replaceExpr(variable,
					IoUtils.getStringFromInputStream(c.getClassLoader().getResourceAsStream(BeanUtils.getResourceClasspath(c, filename)))));
			NotificationUtils.sendMessage(mailMessage);
		} catch (final IOException e) {
			throw HandleException.wrapException(e);
		}
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(UserRegisterRegistry.userRegister).getResourceHomePath();
	}
}
