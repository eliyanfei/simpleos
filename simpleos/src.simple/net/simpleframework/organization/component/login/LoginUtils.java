package net.simpleframework.organization.component.login;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.SimpleosUtil;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class LoginUtils {
	public static final String BEAN_ID = "login_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	public static String getLoginToolbar(final ComponentParameter nComponentParameter) {
		final ArrayList<String> al = new ArrayList<String>();
		final StringBuilder sb = new StringBuilder();
		boolean flag = true;
		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\"><tr>");
		sb.append("<td>");
		if ((Boolean) nComponentParameter.getBeanProperty("showAutoLogin")) {
			sb.append("<input type=\"checkbox\" id=\"_autoLogin\" name=\"_autoLogin\"");
			sb.append(" value=\"true\" style=\"vertical-align:middle;\"/>");
			sb.append("<label style=\"cursor: pointer; vertical-align:middle;\"");
			sb.append(" for=\"_autoLogin\">#(login.1)</label>");
			flag = false;
		}
		sb.append("</td>");
		sb.append("<td align=\"right\">");
		if ((Boolean) nComponentParameter.getBeanProperty("showGetPassword")) {
			final StringBuilder sb2 = new StringBuilder();
			sb2.append("<a onclick=\"$Actions['getPasswordWindow']();\">#(login.2)</a>");
			al.add(sb2.toString());
			flag = false;
		}
		final String registAction = (String) nComponentParameter.getBeanProperty("registAction");
		if (StringUtils.hasText(registAction) && !"false".equalsIgnoreCase(registAction) && "true".equals(SimpleosUtil.attrMap.get("sys_register"))) {
			final StringBuilder sb2 = new StringBuilder();
			sb2.append("<a onclick=\"").append(registAction).append("\">#(login.8)</a>");
			al.add(sb2.toString());
			flag = false;
		}
		sb.append(StringUtils.join(al, HTMLBuilder.SEP));
		sb.append("</td>");
		sb.append("</tr></table>");
		if (flag) {
			return "";
		}
		return sb.toString();
	}

	public static String getLocationPath() {
		return getHomePath() + "/jsp/location.jsp";
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(LoginRegistry.login).getResourceHomePath();
	}
}
