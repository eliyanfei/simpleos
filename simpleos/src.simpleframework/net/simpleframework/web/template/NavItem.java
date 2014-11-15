package net.simpleframework.web.template;

import net.simpleframework.web.page.PageUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NavItem {

	public static NavItem home = new NavItem("首页", PageUtils.pageConfig.getPagePath());

	private final String text, url;

	public NavItem(final String text, final String url) {
		this.text = text;
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public String getUrl() {
		return url;
	}
}
