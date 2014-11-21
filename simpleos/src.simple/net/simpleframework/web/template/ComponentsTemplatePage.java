package net.simpleframework.web.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;

import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.component.login.LoginUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.AbstractPageView;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.AbstractMenuHandle;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ComponentsTemplatePage extends AbstractPageView {
	protected Collection<MenuItem> getMenuItems(final ComponentParameter compParameter,
			final MenuItem menuItem) {
		if (menuItem == null) {
			final String pPath = PageUtils.pageConfig.getPagePath();
			final ArrayList<MenuItem> al = new ArrayList<MenuItem>();
			// al.add(createMenuItem(compParameter, "首页", pPath));
			// al.add(createMenuSepItem(compParameter));
			final MenuItem sMenu = createMenuItem(compParameter, "系统管理", null);
			sMenu.getChildren().add(createMenuItem(compParameter, "账号管理", pPath + "/org/UserMgrPage"));
			sMenu.getChildren().add(createMenuSepItem(compParameter));
			sMenu.getChildren().add(createMenuItem(compParameter, "角色管理", pPath + "/org/JobMgrPage"));
			al.add(sMenu);
			return al;
		}
		return null;
	}

	public boolean isShowMenu(final PageParameter pageParameter) {
		return true;
	}

	public boolean isShowFooter(final PageParameter pageParameter) {
		return true;
	}

	public String logoTitle(final PageParameter pageParameter) {
		return LocaleI18n.getMessage("ComponentsTemplatePage.0");
	}

	public String loginBar(final PageParameter pageParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<a href='").append(PageUtils.pageConfig.getPagePath())
				.append("/portal-my-MyPortalPage'>")
				.append(LocaleI18n.getMessage("ComponentsTemplatePage.3")).append("</a>");
		sb.append(HTMLBuilder.SEP);
		final IAccount account = AccountSession.getLogin(pageParameter.getSession());
		if (account == null) {
			sb.append("<a onclick=\"$Actions.loc('").append(LoginUtils.getLocationPath())
					.append("')\">").append(LocaleI18n.getMessage("ComponentsTemplatePage.1"))
					.append("</a>");
		} else {
			sb.append("<a onclick=\"$Actions['editUserWindow']();\">").append(account.user())
					.append("</a><span> ( </span><a onclick=\"$Actions['ajaxLogout']();\">")
					.append(LocaleI18n.getMessage("ComponentsTemplatePage.2"))
					.append("</a><span> )</span>");
		}
		return sb.toString();
	}

	protected List<NavItem> navItems(final PageParameter pageParameter) {
		final List<NavItem> items = new ArrayList<NavItem>();
		items.add(NavItem.home);
		return items;
	}

	public String navBar(final PageParameter pageParameter) {
		final StringBuilder sb = new StringBuilder();
		final List<NavItem> items = navItems(pageParameter);
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				final NavItem item = items.get(i);
				if (i > 0) {
					sb.append("<span class='nav_img'></span>");
				}
				sb.append("<a");
				final String url = item.getUrl();
				if (StringUtils.hasText(url)) {
					sb.append(" href='").append(item.getUrl()).append("'");
				}
				sb.append(" class='nav_title'>").append(item.getText()).append("</a>");
			}
		}
		return sb.toString();
	}

	private static ComponentsTemplatePage templatePage;

	@Override
	protected void init(final ServletContext servletContext, final PageDocument pageDocument) {
		super.init(servletContext, pageDocument);
		templatePage = this;
	}

	public static class MainMenu extends AbstractMenuHandle {
		@Override
		public Collection<MenuItem> getMenuItems(final ComponentParameter compParameter,
				final MenuItem menuItem) {
			return templatePage.getMenuItems(compParameter, menuItem);
		}
	}
}
