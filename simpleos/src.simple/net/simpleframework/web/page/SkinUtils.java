package net.simpleframework.web.page;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.itsite.ItSiteUtil;
import net.itsite.utils.StringsUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class SkinUtils {
	public final static String DEFAULT_SKIN = "blue";

	private final static String SESSION_NAME_SKIN = "__skin";

	public static Map<String, String> skinMap = new LinkedHashMap<String, String>();

	static {
		skinMap.put("blue", "#(Itsite.skin.blue)");
		skinMap.put("green", "#(Itsite.skin.green)");
		skinMap.put("red", "#(Itsite.skin.red)");
	}

	public static Map<String, String> getSkinList() {
		return skinMap;
	}

	public static String getSkin(final PageRequestResponse requestResponse, final String userSkin) {
		String sessionSkin = (String) requestResponse.getSessionAttribute(SkinUtils.SESSION_NAME_SKIN);
		if (sessionSkin == null) {
			final IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
			if (account != null) {
				setSessionSkin(requestResponse.getSession(), account.getSkin());
			}
		}
		if (!StringsUtils.isNotBlank1(sessionSkin)) {
			sessionSkin = DEFAULT_SKIN;
		}
		return StringUtils.text(sessionSkin, userSkin);
	}

	public static void setSessionSkin(final HttpSession httpSession, final String skin) {
		httpSession.setAttribute(SkinUtils.SESSION_NAME_SKIN, skin);
	}

	public static void setComponentSkin(final String registryName, final String skin) {
		final AbstractComponentRegistry registry = AbstractComponentRegistry.getRegistry(registryName);
		if (registry != null) {
			registry.getComponentResourceProvider().setSkin(skin);
		}
	}
}
