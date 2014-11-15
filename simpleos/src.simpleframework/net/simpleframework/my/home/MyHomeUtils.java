package net.simpleframework.my.home;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.id.ID;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.IConstants;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class MyHomeUtils {
	public static IHomeApplicationModule applicationModule;

	public static String deployPath;

	public static String getCssPath(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(deployPath).append("css/").append(applicationModule.getSkin(requestResponse));
		return sb.toString();
	}

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static HomeTabsBean getHomeTab(final Object id) {
		return getTableEntityManager(HomeTabsBean.class).queryForObjectById(id, HomeTabsBean.class);
	}

	public static HomeTabsBean getFirstHomeTab(final PageRequestResponse requestResponse) {
		return getFirstHomeTab(requestResponse,
				MySpaceUtils.getAccountAware().getAccount(requestResponse));
	}

	static HomeTabsBean getFirstHomeTab(final PageRequestResponse requestResponse,
			final IAccount account) {
		if (account != null) {
			final ExpressionValue ev = new ExpressionValue("userid=? and defaulttab=?", new Object[] {
					account.getId(), true });
			HomeTabsBean homeTabs = getTableEntityManager(HomeTabsBean.class).queryForObject(ev,
					HomeTabsBean.class);
			if (homeTabs == null) {
				homeTabs = insertHomeTab(account.getId(),
						LocaleI18n.getMessage("DefaultHomeApplicationModule.0"), true, null);
			}
			return homeTabs;
		} else {
			return null;
		}
	}

	static HomeTabsBean insertHomeTab(final ID userId, final String tabText,
			final boolean defaulttab, final String description) {
		final HomeTabsBean homeTab = new HomeTabsBean();
		homeTab.setUserId(userId);
		homeTab.setCreateDate(new Date());
		homeTab.setTabText(tabText);
		homeTab.setDefaulttab(defaulttab);
		homeTab.setDescription(description);
		getTableEntityManager(HomeTabsBean.class).insertTransaction(homeTab,
				new TableEntityAdapter() {
					@Override
					public void afterInsert(final ITableEntityManager manager, final Object[] objects) {
						final HomePortalBean homePortal = new HomePortalBean();
						homePortal.setId(homeTab.getId());
						try {
							final Reader reader = new InputStreamReader(
									getClass().getClassLoader()
											.getResourceAsStream(
													"net/simpleframework/my/home/"
															+ (defaulttab ? "default_layout.xml"
																	: "template_layout.xml")), IConstants.UTF8);
							homePortal.setLayoutLob(reader);
						} catch (final UnsupportedEncodingException e) {
						}
						getTableEntityManager(HomePortalBean.class).insert(homePortal);
					}
				});
		return homeTab;
	}

	public static String tabs(final PageRequestResponse requestResponse) {
		final ArrayList<TabHref> al = new ArrayList<TabHref>();
		for (final HomeTabsBean homeTab : applicationModule.getHomeTabs(requestResponse)) {
			final TabHref tabHref = new TabHref(homeTab.getTabText(), applicationModule.getTabUrl(
					requestResponse, homeTab));
			final StringBuilder html2 = new StringBuilder();
			final String desc = homeTab.getDescription();
			if (StringUtils.hasText(desc)) {
				html2.append("<div style=\"display: none;\">");
				html2.append(HTMLUtils.convertHtmlLines(desc)).append("</div>");
			}
			html2.append("<span class=\"menu\" id=\"m").append(homeTab.getId()).append("\"></span>");
			tabHref.setHtml2(html2.toString());
			al.add(tabHref);
		}
		return TabsUtils.tabs(requestResponse, al.toArray(new TabHref[al.size()]));
	}
}
