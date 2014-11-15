package net.simpleframework.web;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.content.bbs.BbsUtils;
import net.simpleframework.content.blog.BlogUtils;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.core.IApplicationModule;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IWebApplicationModule extends IApplicationModule {

	String getSkin(PageRequestResponse requestResponse);

	String getApplicationUrl(PageRequestResponse requestResponse);

	public static class Utils {
		private static final String MANAGER_KEY = "AbstractWebApplicationModule.isManager";

		public static boolean isManager(final PageRequestResponse requestResponse,
				final IWebApplicationModule module) {
			Boolean member = (Boolean) requestResponse.getRequestAttribute(MANAGER_KEY);
			if (member == null) {
				requestResponse.setRequestAttribute(
						MANAGER_KEY,
						member = OrgUtils.isMember(module.getManager(requestResponse),
								requestResponse.getSession()));
			}
			return member;
		}

		private static Map<EFunctionModule, IWebApplicationModule> modules;

		public static IWebApplicationModule getApplicationModule(final EFunctionModule aModule) {
			if (modules == null) {
				modules = new HashMap<EFunctionModule, IWebApplicationModule>();
				modules.put(EFunctionModule.bbs, BbsUtils.applicationModule);
				modules.put(EFunctionModule.blog, BlogUtils.applicationModule);
				modules.put(EFunctionModule.news, NewsUtils.applicationModule);
			}
			return modules.get(aModule);
		}
	}
}
