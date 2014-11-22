package net.simpleos.mvc.news;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.news.News;
import net.simpleframework.content.news.NewsUtils;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-19下午01:06:02
 */
public class MVCNewsUtils {
	/**
	 * 获取审核的新闻
	 * @param requestResponse
	 * @return
	 */
	public static String getAuditNews(final PageRequestResponse requestResponse, int type) {
		IQueryEntitySet<Map<String, Object>> qs = NewsUtils.getTableEntityManager(News.class).query(
				new ExpressionValue("status=?", new Object[] { EContentStatus.audit }));
		final int count = qs.getCount();
		if (count == 0)
			return "";
		final StringBuffer sb = new StringBuffer();
		sb.append("<sup class=\"highlight \">");
		sb.append(count).append("</sup>");
		return sb.toString();
	}
}
