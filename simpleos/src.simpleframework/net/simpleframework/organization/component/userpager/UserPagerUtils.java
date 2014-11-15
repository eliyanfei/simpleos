package net.simpleframework.organization.component.userpager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.organization.account.Exp;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class UserPagerUtils {
	public static final String BEAN_ID = "user_@bid";

	public static ComponentParameter getComponentParameter(final PageRequestResponse requestResponse) {
		return ComponentParameter.get(requestResponse, BEAN_ID);
	}

	public static ComponentParameter getComponentParameter(final HttpServletRequest request, final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}

	static String[] userProperties = new String[] { "text", "sex", "blood", "hometown", "email", "homePhone", "officePhone", "mobile", "address",
			"postcode", "qq", "msn", "description", "homepage" };

	public static String getExpIcon(final Exp exp) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<span class=\"jifen\" title=\"").append(exp.getExp()).append("\" style=\"background-position: -");
		sb.append(32 * (exp.getLevel() - 1)).append("px 0;\"></span>");
		return sb.toString();
	}

	public static String getHomePath() {
		return AbstractComponentRegistry.getRegistry(UserPagerRegistry.userPager).getResourceHomePath();
	}

	public static String getCssPath(final PageRequestResponse requestResponse) {
		return AbstractComponentRegistry.getRegistry(UserPagerRegistry.userPager).getCssResourceHomePath(requestResponse);
	}

	public static String xmlUserutils() {
		return getHomePath() + "/jsp/__userutils.xml";
	}

	public static String cssUserutils(final PageRequestResponse requestResponse) {
		return getCssPath(requestResponse) + "/userutils.css";
	}
}
