package net.simpleos.backend.links;

import java.util.Map;

import net.itsite.utils.LangUtils;
import net.itsite.utils.StringsUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.sysmgr.dict.SysDict;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 
 * @author 李岩飞 2013-3-28下午04:17:22
 */
public class LinksUtils {
	public static ILinksAppModule appModule = null;

	public static String queryLinks(final PageRequestResponse requestResponse, Map<String, String> map) {
		final StringBuffer b1 = new StringBuffer();
		if ("custom".equals(map.get("links.links_linksType"))) {
			b1.append(StringsUtils.trimNull(map.get("links.links_homeLinks"), ""));
		} else {
			IQueryEntitySet<LinksBean> qs = appModule.queryBean(new ExpressionValue("1=1 order by oorder"), LinksBean.class);
			LinksBean linksBean = null;
			SysDict sysDict = DictUtils.getSysDictByName("links");
			if (sysDict == null) {
				sysDict = new SysDict();
				sysDict.setText("10");
			}
			int count = LangUtils.toInt(sysDict.getText(), 10);
			qs.setCount(count);
			while ((linksBean = qs.next()) != null) {
				b1.append("<a style='color:" + linksBean.getColor() + ";' href='" + linksBean.getUrl() + "' target='blank'>")
						.append(linksBean.getTitle()).append("</a>");
			}
			b1.append("<a  href='/links.html' target='blank'>").append("更多链接").append("</a>");
		}
		return b1.toString();
	}

}
