package net.simpleframework.content.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.ContentLayoutUtils;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.EContentType;
import net.simpleframework.content.component.newspager.NewsCatalog;
import net.simpleframework.content.component.newspager.NewsPagerUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
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
public abstract class NewsUtils {
	public static INewsApplicationModule applicationModule;
	public static List<NewsCatalog> newsCatalogs = new ArrayList<NewsCatalog>();

	public static String deployPath;

	public static void initNewsCatalog() {
		try {
			IQueryEntitySet<NewsExtCatalog> qs = getTableEntityManager(NewsExtCatalog.class).query(new ExpressionValue("1=1 order by oorder"),
					NewsExtCatalog.class);
			NewsExtCatalog catalog = null;
			final List<NewsCatalog> list = new ArrayList<NewsCatalog>();
			while ((catalog = qs.next()) != null) {
				NewsCatalog catalog2 = new NewsCatalog();
				catalog2.setText(catalog.getText());
				catalog2.setId(catalog.getId());
				list.add(catalog2);
			}
			newsCatalogs.clear();
			newsCatalogs.addAll(list);
			list.clear();
		} catch (Exception e) {
		}
	}

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

	public static IDataObjectQuery<News> queryNews(final PageRequestResponse requestResponse, final Object catalog, final EContentType contentType,
			final TimeDistance timeDistance, final int order) {
		return queryNews(requestResponse, catalog, contentType, timeDistance, order, -1);
	}

	public static IDataObjectQuery<News> queryNews(final PageRequestResponse requestResponse, final Object catalog, final EContentType contentType,
			final TimeDistance timeDistance, final int order, final int _tab_param) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		sql.append("status=?");
		al.add(EContentStatus.publish);
		if (catalog != null) {
			if (ConvertUtils.toInt(catalog) > 0) {
				sql.append(" and catalogid=?");
			} else {
				sql.append(" and catalogid<>?");
			}
			al.add(Math.abs(ConvertUtils.toInt(catalog)));
		}
		if (timeDistance != null) {
			sql.append(" and createdate>?");
			al.add(DateUtils.getTimeCalendar(timeDistance).getTime());
		}
		if (_tab_param == 3) {
			sql.append(" and lastUpdate>?");
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

		if (_tab_param == 0) {
			sql.append(" order by createdate desc");
		} else if (_tab_param == 2) {
			sql.append(" order by views desc");
		} else if (_tab_param == 3) {
			sql.append(" order by remarks desc");
		} else if (_tab_param == 5) {
			sql.append(" order by remarks desc");
		} else if (order == 0) {
			sql.append(" order by createdate desc");
		} else if (order == 1) {
			sql.append(" order by views desc");
		} else if (order == 3) {
			sql.append(" order by ttop desc ,createdate desc");
		}
		return getTableEntityManager(News.class).query(new ExpressionValue(sql.toString(), al.toArray()), News.class);
	}

	public static IDataObjectQuery<NewsRemark> queryRemarks(final PageRequestResponse requestResponse) {
		return ContentLayoutUtils.getQueryByExpression(requestResponse, applicationModule, NewsRemark.class, new ExpressionValue(
				"1=1 order by createdate desc"));
	}

	public static void doStatRebuild() {
		NewsPagerUtils.doNewsStatRebuild(applicationModule, EFunctionModule.news);
	}

	/**************************** jsp utils ****************************/

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

	public static Map<String, List<NewsRemark>> getRelateRemark(NewsRemark remark) {
		Map<String, List<NewsRemark>> map = new HashMap<String, List<NewsRemark>>();
		ITableEntityManager manager = getTableEntityManager(NewsRemark.class);
		//获取父节点
		IQueryEntitySet<NewsRemark> qs = manager.query(new ExpressionValue("id=" + remark.getParentId()), NewsRemark.class);
		List<NewsRemark> list = null;
		NewsRemark temp = null;
		list = new ArrayList<NewsRemark>();
		while ((temp = qs.next()) != null) {
			list.add(temp);
		}
		map.put("p", list);

		//获取子节点
		qs = manager.query(new ExpressionValue("parentId=" + remark.getId()), NewsRemark.class);
		list = new ArrayList<NewsRemark>();
		while ((temp = qs.next()) != null) {
			list.add(temp);
		}
		map.put("c", list);
		return map;
	}

	public static String buildRelateRemark(NewsRemark remark, ComponentParameter parameter) {
		Map<String, List<NewsRemark>> map = getRelateRemark(remark);
		final StringBuffer buf = new StringBuffer();
		int idx = 0;
		List<NewsRemark> list = map.get("p");
		if (list != null) {
			for (NewsRemark nr : list) {
				appendRelateRemark(buf, nr, parameter, "remark_item_" + (idx++));
			}
		}
		appendRelateRemark(buf, remark, parameter, "remark_item_" + (idx++));
		list = map.get("c");
		if (list != null) {
			for (NewsRemark nr : list) {
				appendRelateRemark(buf, nr, parameter, "remark_item_" + idx);
			}
		}
		return buf.toString();
	}

	private static void appendRelateRemark(StringBuffer buf, NewsRemark remark, ComponentParameter parameter, final String className) {
		buf.append("<div class='" + className + "'>").append(remark.getContent());
		buf.append("<div class='hsep'>");
		buf.append(ContentUtils.getAccountAware().wrapAccountHref(parameter, remark.getUserId()));
		buf.append(" , 评论于 ").append(DateUtils.getRelativeDate(remark.getCreateDate()));
		buf.append("</div></div>");
	}
}
