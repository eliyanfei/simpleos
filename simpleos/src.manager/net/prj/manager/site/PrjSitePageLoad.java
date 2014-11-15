package net.prj.manager.site;

import java.util.List;
import java.util.Map;

import net.itniwo.commons.StringsUtils;
import net.itsite.impl.PrjColumns;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午04:32:04
 */
public class PrjSitePageLoad extends DefaultPageHandle {
	/**
	 * 站点数据还原
	 */
	public void siteLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = PrjSiteUtils.appModule.getPrjColumns("site");
		Map<String, String> map = PrjMgrUtils.loadCustom("site");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(map.get(key), ""));
		}
	}

	/**
	 * 站点数据还原
	 */
	public void linksLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = PrjSiteUtils.appModule.getPrjColumns("links");
		Map<String, String> map = PrjMgrUtils.loadCustom("links");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(map.get(key), ""));
		}
	}
}
