package net.simpleframework.content.blog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.IWebApplicationModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyBlogPagerHandle extends AbstractBlogPagerHandle {

	@Override
	public String getNavigateHTML(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getNavigateHTML(compParameter));
		final IAccount account = ContentUtils.getAccountAware().getAccount(compParameter);
		if (account != null) {
			final IUser user = account.user();
			if (user != null) {
				sb.append(HTMLBuilder.NAV);
				sb.append("<a href=\"")
						.append(BlogUtils.applicationModule.getBlogUrl(compParameter, user))
						.append("\">").append(user.getText()).append("</a>");
			}
			final BlogCatalog blogCatalog = BlogCatalogHandle.getBlogCatalogByRequest(compParameter);
			if (blogCatalog != null) {
				sb.append(HTMLBuilder.NAV).append(blogCatalog.getText());
			}
		}
		wrapNavImage(compParameter, sb);
		return sb.toString();
	}

	private List<MenuItem> contextMenu;

	@Override
	public List<MenuItem> getContextMenu(final ComponentParameter compParameter,
			final MenuBean menuBean) {
		if (contextMenu == null) {
			contextMenu = new ArrayList<MenuItem>(super.getContextMenu(compParameter, menuBean));
			final ArrayList<MenuItem> removes = new ArrayList<MenuItem>();
			for (final MenuItem item : contextMenu) {
				final String jsCallback = item.getJsSelectCallback();
				if (StringUtils.hasText(jsCallback) && jsCallback.contains(".edit2(")) {
					removes.add(item);
				}
			}
			contextMenu.removeAll(removes);
		}
		return contextMenu;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, OrgUtils.um().getUserIdParameterName());
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		final IAccount login = ContentUtils.getAccountAware().getAccount(compParameter);
		if (login == null) {
			sql.append("1=2");
		} else {
			sql.append("userid=?");
			al.add(login.getId());
			final BlogCatalog blogCatalog = BlogCatalogHandle.getBlogCatalogByRequest(compParameter);
			if (blogCatalog != null) {
				sql.append(" and catalogid=?");
				al.add(blogCatalog.getId());
			}
		}
		sql.append(" order by ttop desc, createdate desc");
		final ExpressionValue ev2 = new ExpressionValue(sql.toString(), al.toArray());
		return getTableEntityManager(compParameter).query(ev2, getEntityBeanClass());
	}

	@Override
	protected boolean isEditable(final PageRequestResponse requestResponse) {
		return IWebApplicationModule.Utils.isManager(requestResponse, BlogUtils.applicationModule)
				|| ContentUtils.getAccountAware().isMyAccount(requestResponse);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new BlogTablePagerData(compParameter) {
			@Override
			protected String getTitle(final Blog blog) {
				final StringBuilder sb = new StringBuilder();
				sb.append("<div class=\"lbl\">");
				final EContentStatus status = blog.getStatus();
				if (status != EContentStatus.publish) {
					sb.append("<span style=\"margin-right: 6px;\" class=\"important-tip\">[ ").append(status).append(" ]</span>");
				}
				sb.append(ItSiteUtil.markContent(c, wrapOpenLink(compParameter, blog)));
				sb.append(ItSiteUtil.buildTimeString(blog.getCreateDate()));
				sb.append("<div class=\"nc\">");
				final String img = ContentUtils.getContentImage(compParameter, blog, 64, 64);
				if (img != null) {
					sb.append(img);
				}
				sb.append(ItSiteUtil.markContent(c, ContentUtils.getShortContent(blog, 300, true)));
				sb.append("<div class=\"nd\">");
				sb.append("<span class=\"num\">" + blog.getRemarks() + "</span>&nbsp;评论");
				sb.append("<span style=\"margin: 0px 5px;\">|</span>");
				sb.append("<span class=\"num\">" + blog.getViews() + "</span>&nbsp;阅读");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("</div>");
				return sb.toString();
			}
		};
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter,
			final String jsAction, final Object bean) {
		String jsCallback = super.getJavascriptCallback(compParameter, jsAction, bean);
		if ("delete".equals(jsAction)) {
			jsCallback += "$Actions[\"__my_blog_catalog\"].refresh();";
		}
		return jsCallback;
	}
}
