package net.simpleframework.web.page.component.ui.validatecode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
public class DefaultValidateCodeHandle extends AbstractComponentHandle implements
		IValidateCodeHandle {
	@Override
	public void doValidateCode(final ComponentParameter compParameter, final String code) {
		compParameter.getSession().setAttribute(GEN_CODE, code);
	}

	public static String getErrorString() {
		return LocaleI18n.getMessage("DefaultValidateCodeHandle.0");
	}

	public static String getValidateCode(final HttpSession session) {
		return (String) session.getAttribute(GEN_CODE);
	}

	public static boolean isValidateCode(final HttpServletRequest request, final String inputName) {
		final String validateCode = request.getParameter(inputName);
		return validateCode != null ? validateCode.equalsIgnoreCase(getValidateCode(request
				.getSession())) : true;
	}
}
