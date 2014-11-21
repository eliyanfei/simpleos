package net.simpleos.backend.site;

import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.impl.PrjColumns;
import net.itsite.utils.StringsUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午04:32:04
 */
public class SitePageLoad extends DefaultPageHandle {
	/**
	 * 站点数据还原
	 */
	public void siteLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = SiteUtils.appModule.getPrjColumns("site");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(ItSiteUtil.attrMap.get("site." + key), ""));
		}
	}

	/**
	 * 站点数据还原
	 */
	public void linksLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = SiteUtils.appModule.getPrjColumns("links");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(ItSiteUtil.attrMap.get("links." + key), ""));
		}
	}
}
