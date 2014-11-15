package net.prj.manager.links;

import java.util.Map;

import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 
 * @author 李岩飞
 * 2013-3-28下午04:17:22
 */
public class PrjLinksUtils {
	public static IPrjLinksAppModule appModule = null;

	public static String queryLinks(final PageRequestResponse requestResponse, Map<String, String> map) {
		final StringBuffer b1 = new StringBuffer();
		if ("custom".equals(map.get("links_linksType"))) {
			b1.append(StringsUtils.trimNull(map.get("links_homeLinks"), ""));
		} else {
			IQueryEntitySet<PrjLinksBean> qs = appModule.queryBean(PrjLinksBean.class);
			PrjLinksBean linksBean = null;
			while ((linksBean = qs.next()) != null) {
				b1.append("<a style='color:" + linksBean.getColor() + ";' href='" + linksBean.getUrl() + "' target='blank'>").append(linksBean.getTitle()).append("</a>");
			}
		}
		return b1.toString();
	}

}
