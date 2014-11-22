package net.simpleos.mvc.remark;

import java.util.Date;
import java.util.Map;

import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.blog.BlogRemark;
import net.simpleframework.content.component.remark.RemarkItem;
import net.simpleframework.util.DateUtils;
import net.simpleframework.util.IPAndCity;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class RemarkUtils {
	public static String buildRelateRemark(RemarkItem remark, ComponentParameter parameter) {
		StringBuffer buf = new StringBuffer();
		buf.append(remark.getContent());
		buf.append("<div class='hsep'>");
		buf.append("<div style='float:left;'>");
		buf.append(
				"<a style='margin-right:6px;' href='#' onclick=\"$IT.A('remarkWindowAct','rtype=" + (remark instanceof BlogRemark ? "Blog" : "News")
						+ "&remarkId=" + remark.getId() + "')").append("\">删除</a>");
		buf.append(ContentUtils.getAccountAware().wrapAccountHref(parameter, remark.getUserId()));
		buf.append(" , 评论于 ").append(DateUtils.getRelativeDate(remark.getCreateDate()));
		buf.append("</div><div style='float:right;'>");
		buf.append(remark.getIp()).append("(").append(IPAndCity.getCity(remark.getIp(), true)).append(")");
		buf.append("</div></div>");
		return buf.toString();
	}

	public static String buildRelateRemark(Map<String, Object> remark, ComponentParameter parameter) {
		StringBuffer buf = new StringBuffer();
		buf.append(remark.get("content"));
		buf.append("<div class='hsep'>");
		buf.append("<div style='float:left;'>");
		buf.append("<a style='margin-right:6px;' href='#' onclick=\"$IT.A('remarkWindowAct','rtype=Bbs&remarkId=" + remark.get("id") + "')").append(
				"\">删除</a>");
		buf.append(ContentUtils.getAccountAware().wrapAccountHref(parameter, remark.get("userid")));
		buf.append(" , 评论于 ").append(DateUtils.getRelativeDate((Date) remark.get("createdate")));
		buf.append("</div><div style='float:right;'>");
		buf.append(remark.get("ip")).append("(").append(IPAndCity.getCity((String) remark.get("ip"), true)).append(")");
		buf.append("</div></div>");
		return buf.toString();
	}
}
