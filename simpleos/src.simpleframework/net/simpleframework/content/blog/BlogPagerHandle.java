package net.simpleframework.content.blog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.a.ItSiteUtil;
import net.itsite.user.UserSearchUtils;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.applets.tag.ITagApplicationModule;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.EPagerPosition;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class BlogPagerHandle extends AbstractBlogPagerHandle {

	@Override
	public String getNavigateHTML(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getNavigateHTML(compParameter));
		final Blog blog = getEntityBeanByRequest(compParameter);
		if (blog != null) {
			final IUser user = OrgUtils.um().queryForObjectById(blog.getUserId());
			if (user != null) {
				sb.append(HTMLBuilder.NAV);
				sb.append("<a href=\"").append(getApplicationModule().getBlogUrl(compParameter, user)).append("\">");
				sb.append(user.getText()).append("</a>");
			}
			sb.append(HTMLBuilder.NAV);
			sb.append(LocaleI18n.getMessage("DefaultNewsPagerHandle.0"));
		}
		wrapNavImage(compParameter, sb);
		return sb.toString();
	}

	@Override
	public List<MenuItem> getHeaderMenu(final ComponentParameter compParameter, final MenuBean menuBean) {
		return doManagerHeaderMenu(compParameter, menuBean, this, "blogManagerToolsWindow");
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "t");
		return parameters;
	}

	public String getPagerUrl(ComponentParameter compParameter, EPagerPosition pagerPosition, int pageItems, Map<String, Integer> pageVar) {
		if (true || StringUtils.hasText(compParameter.getRequestParameter("c")) || StringUtils.hasText(compParameter.getRequestParameter("tagId"))
				|| StringUtils.hasText(compParameter.getRequestParameter("t")) || StringUtils.hasText(compParameter.getRequestParameter("od"))
				|| StringUtils.hasText(compParameter.getRequestParameter("time")) || StringUtils.hasText(compParameter.getRequestParameter("status"))
				|| pagerPosition == EPagerPosition.pageNumber) {
			return super.getPagerUrl(compParameter, pagerPosition, pageItems, pageVar);
		}
		if (StringUtils.hasText(compParameter.getRequestParameter("p"))) {
			final StringBuilder sb = new StringBuilder();
			sb.append("/blog/p/");
			final int pageNumber = ConvertUtils.toInt(pageVar.get("pageNumber"), 0);
			final int currentPageNumber = ConvertUtils.toInt(pageVar.get("currentPageNumber"), 0);
			final int pageCount = ConvertUtils.toInt(pageVar.get("pageCount"), 0);
			if (pagerPosition == EPagerPosition.left2) {
				sb.append(1);
			} else if (pagerPosition == EPagerPosition.left) {
				sb.append(currentPageNumber > 1 ? (currentPageNumber - 1) : 1);
			} else if (pagerPosition == EPagerPosition.number) {
				sb.append(pageNumber);
			} else if (pagerPosition == EPagerPosition.right) {
				sb.append(currentPageNumber >= pageCount ? pageCount : currentPageNumber + 1);
			} else if (pagerPosition == EPagerPosition.right2) {
				sb.append(pageCount);
			} else if (pagerPosition == EPagerPosition.pageItems) {
				sb.append(1);
			}
			sb.append(".html");
			return sb.toString();
		}
		return super.getPagerUrl(compParameter, pagerPosition, pageItems, pageVar);
	}

	@Override
	protected boolean dataSplit() {
		return true;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final String c = PageUtils.toLocaleString(compParameter.getRequestParameter("c"));
		if (StringUtils.hasText(c)) {
			UserSearchUtils.createSearch(compParameter, EFunctionModule.blog, c);
			return createLuceneManager(compParameter).getLuceneQuery(c);
		} else {
			final ITableEntityManager blog_mgr = getTableEntityManager(compParameter);
			final String tag = compParameter.getRequestParameter(ITagApplicationModule._TAG_ID);
			final StringBuilder sql = new StringBuilder();
			final ArrayList<Object> al = new ArrayList<Object>();
			if (StringsUtils.isNotBlank(tag)) {
				appendTagsSQL(compParameter, sql, al, tag);
				sql.append(" and a.status=" + EContentStatus.publish.ordinal());
				sql.append(" order by  a.createdate desc");
				System.out.println(sql.toString());
				return blog_mgr.query(new SQLValue(sql.toString(), al.toArray()), getEntityBeanClass());
			}
			final String t = compParameter.getRequestParameter("t");
			if ("attention".equals(t)) {
				return blog_mgr.query(AttentionUtils.attentionSQLValue(compParameter, getFunctionModule()), getEntityBeanClass());
			}
			return blog_mgr.query(createSqlValue(compParameter), getEntityBeanClass());
		}
	}

	@Override
	protected SQLValue createSqlValue(ComponentParameter compParameter) {
		final StringBuilder sql = new StringBuilder();
		final ArrayList<Object> al = new ArrayList<Object>();
		final String t = compParameter.getRequestParameter("t");
		sql.append("select * from simple_blog ");
		boolean aloneLimit = false;
		if ("recommended".equals(t)) {
			sql.append(" where ttype=?");
			al.add(EContentType.recommended);
			if (compParameter.getStart() > ItSiteUtil.aloneLimit && dataSplit()) {
				aloneLimit = true;
				sql.append("id <= (select id from simple_blog where ttype=" + EContentType.recommended.ordinal() + " order by createdate desc limit ")
						.append(compParameter.getStart()).append(",1) and ");
			}
		} else if ("audit".equals(t)) {
			if (ItSiteUtil.isManage(compParameter)) {
				sql.append(" where status=?");
				al.add(EContentStatus.audit);
			}
		} else {
			sql.append(" where ");
			if (compParameter.getStart() > ItSiteUtil.aloneLimit && dataSplit()) {
				aloneLimit = true;
				sql.append("id <= (select id from simple_blog where status=" + EContentStatus.publish.ordinal() + " order by createdate desc limit ")
						.append(compParameter.getStart()).append(",1) and ");
			}
			sql.append(" status=?");
			al.add(EContentStatus.publish);
		}
		sql.append(" order by createdate desc");
		return new SQLValue(sql.toString(), al.toArray()).setAloneLimit(aloneLimit);
	}

	@Override
	protected boolean isEditable(final PageRequestResponse requestResponse) {
		return false;
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new BlogTablePagerData(compParameter) {
			@Override
			protected String getTitle(final Blog blog) {
				final StringBuilder sb = new StringBuilder();
				final EContentStatus status = blog.getStatus();
				sb.append("<div class=\"lbl\">");
				if (status != EContentStatus.publish) {
					sb.append("<span style=\"margin-right: 6px;\" class=\"important-tip\">[ ").append(status).append(" ]</span>");
				}
				sb.append(ItSiteUtil.markContent(c, wrapOpenLink(compParameter, blog)));
				sb.append(ItSiteUtil.buildTimeString(blog.getCreateDate()));
				sb.append("<div class=\"nc\">");
				sb.append(ContentUtils.getAccountAware().wrapImgAccountHref(compParameter, blog.getUserId()));
				sb.append(ItSiteUtil.markContent(c, ContentUtils.getShortContent(blog, 300, true)));
				sb.append("<div class=\"nd\">");
				sb.append("<span class=\"num\">" + blog.getRemarks() + "</span>&nbsp;评论");
				sb.append("<span style=\"margin: 0px 5px;\">|</span>");
				sb.append("<span class=\"num\">" + blog.getViews() + "</span>&nbsp;阅读 By ");
				sb.append(ContentUtils.getAccountAware().wrapAccountHref(compParameter, blog.getUserId(), blog.getUserText()));
				sb.append("</div>");
				sb.append("</div>");
				sb.append("</div>");
				return sb.toString();
			}
		};
	}
}
