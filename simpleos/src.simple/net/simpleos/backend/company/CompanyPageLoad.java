package net.simpleos.backend.company;

import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.impl.PrjColumns;
import net.itsite.utils.StringsUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleos.backend.BackendUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午04:32:04
 */
public class CompanyPageLoad extends DefaultPageHandle {
	/**
	 * 站点数据还原
	 */
	public void contactLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = CompanyUtils.appModule.getPrjColumns("contact");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(ItSiteUtil.attrMap.get("contact." + key), ""));
		}
	}

	/**
	 * 站点数据还原
	 */
	public void companyLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = CompanyUtils.appModule.getPrjColumns("company");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(ItSiteUtil.attrMap.get("company." + key), ""));
		}
	}
}
