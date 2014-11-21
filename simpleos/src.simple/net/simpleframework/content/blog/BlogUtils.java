package net.simpleframework.content.blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.ContentLayoutUtils;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.newspager.NewsPagerUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.DateUtils.TimeDistance;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class BlogUtils {
	public static IBlogApplicationModule applicationModule;

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

	public static String getTemplatePage(final ComponentParameter compParameter) {
		if (compParameter.componentBean == null) {
			compParameter.componentBean = applicationModule.getComponentBean(compParameter);
			HTTPUtils.putParameter(compParameter.request, NewsPagerUtils.BEAN_ID, compParameter.componentBean.hashId());
		}
		return applicationModule.getTemplatePage(compParameter);
	}

	public static IQueryEntitySet<?> queryBlogs(final PageRequestResponse requestResponse, final EContentType contentType,
			final TimeDistance timeDistance, final boolean spaceAccount, final int order) {
		return queryBlogs(requestResponse, contentType, timeDistance, spaceAccount, order, -1);
	}

	public static IQueryEntitySet<?> queryBlogs(final PageRequestResponse requestResponse, final EContentType contentType,
			final TimeDistance timeDistance, final boolean spaceAccount, final int order, final int _tab_param) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		sql.append("status=?");
		al.add(EContentStatus.publish);
		if (timeDistance != null) {
			sql.append(" and createdate>?");
			al.add(DateUtils.getTimeCalendar(timeDistance).getTime());
		}
		if (_tab_param == 3) {
			sql.append(" and createdate>?");
			al.add(DateUtils.getTimeCalendar(TimeDistance.month).getTime());
		}
		if (contentType != null) {
			sql.append(" and ttype=?");
			al.add(contentType);
		}
		if (_tab_param == 1) {
			sql.append(" and ttype=?");
			al.add(EContentType.recommended);
		}
		if (spaceAccount) {
			final IAccount account = ContentUtils.getAccountAware().getAccount(requestResponse);
			if (account != null) {
				sql.append(" and userid=?");
				al.add(account.getId());
			}
		}

		if (_tab_param == 0) {
			sql.append(" order by createdate desc");
		} else if (_tab_param == 2) {
			sql.append(" order by views desc");
		} else if (_tab_param == 3) {
			sql.append(" order by remarks desc");
		} else if (order == 0) {
			sql.append(" order by createdate desc");
		} else if (order == 1) {
			sql.append(" order by views desc");
		} else if (order == 3) {
			sql.append(" order by createdate desc");
		}
		return getTableEntityManager(Blog.class).query(
				new Object[] { "createDate", "topic", "id", "views", "remarks", "attentions", "topic", "id", "userId", "lastUpdate", "lastUserId" },
				new ExpressionValue(sql.toString(), al.toArray()), Blog.class);
	}

	public static IDataObjectQuery<BlogRemark> queryRemarks(final PageRequestResponse requestResponse) {
		IDataObjectValue ev = null;
		if (ConvertUtils.toBoolean(requestResponse.getRequestParameter("homeAccount"), false)) {
			final IAccount account = ContentUtils.getAccountAware().getAccount(requestResponse);
			if (account != null) {
				final String news_name = getTableEntityManager(Blog.class).getTablename();
				final String remark_name = getTableEntityManager(BlogRemark.class).getTablename();
				final StringBuilder sql = new StringBuilder();
				sql.append("select * from ").append(remark_name).append(" inner join ");
				sql.append(news_name).append(" on ").append(news_name).append(".id=").append(remark_name);
				sql.append(".documentid where ").append(news_name).append(".userid=? order by ");
				sql.append(remark_name).append(".createdate desc");
				ev = new SQLValue(sql.toString(), new Object[] { account.getId() });
			}
		} else {
			ev = new ExpressionValue("1=1 order by createdate desc");
		}
		return ContentLayoutUtils.getQueryByExpression(requestResponse, applicationModule, BlogRemark.class, ev);
	}

	public static void doStatRebuild() {
		NewsPagerUtils.doNewsStatRebuild(applicationModule, EFunctionModule.blog);

		// catalog rebuild
		final ITableEntityManager blog_mgr = getTableEntityManager(Blog.class);
		final ITableEntityManager catalog_mgr = getTableEntityManager(BlogCatalog.class);
		final String blog_name = blog_mgr.getTablename();
		final String catalog_name = catalog_mgr.getTablename();
		final StringBuilder sql = new StringBuilder();
		sql.append("update ").append(catalog_name);
		sql.append(" t set blogs=(select count(id) from ").append(blog_name);
		sql.append(" where catalogid=t.id)");
		catalog_mgr.execute(new SQLValue(sql.toString()));
		catalog_mgr.reset();
	}

	final static Map<String, String> orderData = new LinkedHashMap<String, String>();
	static {
		orderData.put("time", "时间");
		orderData.put("view", "阅读");
		orderData.put("remark", "评论");
		orderData.put("attention", "关注");
	}

	public static String tabs13(PageRequestResponse requestResponse) {
		final StringBuffer buf = new StringBuffer();
		final String odValue = StringUtils.text(requestResponse.getRequestParameter("od"), "time");
		final String t = requestResponse.getRequestParameter("t");
		String applicationUrl = applicationModule.getApplicationUrl(requestResponse);
		if (t != null) {
			return buf.toString();
		}
		int i = 0;
		for (final String od : orderData.keySet()) {
			buf.append("<a hidefocus=\"hidefocus\"");
			buf.append(" href=\"" + WebUtils.addParameters(applicationUrl, "od=" + od) + "\"");
			if (od.equals(odValue)) {
				buf.append(" class=\"a2 nav_arrow\"");
			}
			buf.append(">").append(orderData.get(od)).append("</a>");
			if (i++ != orderData.size() - 1) {
				buf.append("<span style=\"margin: 0px 4px;\">|</span>");
			}
		}
		return buf.toString();
	}

	public static Map<String, List<BlogRemark>> getRelateRemark(BlogRemark remark) {
		Map<String, List<BlogRemark>> map = new HashMap<String, List<BlogRemark>>();
		ITableEntityManager manager = getTableEntityManager(BlogRemark.class);
		//获取父节点
		IQueryEntitySet<BlogRemark> qs = manager.query(new ExpressionValue("id=" + remark.getParentId()), BlogRemark.class);
		List<BlogRemark> list = null;
		BlogRemark temp = null;
		list = new ArrayList<BlogRemark>();
		while ((temp = qs.next()) != null) {
			list.add(temp);
		}
		map.put("p", list);

		//获取子节点
		qs = manager.query(new ExpressionValue("parentId=" + remark.getId()), BlogRemark.class);
		list = new ArrayList<BlogRemark>();
		while ((temp = qs.next()) != null) {
			list.add(temp);
		}
		map.put("c", list);
		return map;
	}

	public static String buildRelateRemark(BlogRemark remark, ComponentParameter parameter) {
		Map<String, List<BlogRemark>> map = getRelateRemark(remark);
		final StringBuffer buf = new StringBuffer();
		int idx = 0;
		List<BlogRemark> list = map.get("p");
		if (list != null) {
			for (BlogRemark nr : list) {
				appendRelateRemark(buf, nr, parameter, "remark_item_" + (idx++));
			}
		}
		appendRelateRemark(buf, remark, parameter, "remark_item_" + (idx++));
		list = map.get("c");
		if (list != null) {
			for (BlogRemark nr : list) {
				appendRelateRemark(buf, nr, parameter, "remark_item_" + idx);
			}
		}
		return buf.toString();
	}

	private static void appendRelateRemark(StringBuffer buf, BlogRemark remark, ComponentParameter parameter, final String className) {
		buf.append("<div class='" + className + "'>").append(remark.getContent());
		buf.append("<div class='hsep'>");
		buf.append(ContentUtils.getAccountAware().wrapAccountHref(parameter, remark.getUserId()));
		buf.append(" , 评论于 ").append(DateUtils.getRelativeDate(remark.getCreateDate()));
		buf.append("</div></div>");
	}
}
