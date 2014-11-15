package net.prj.manager.company;

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
public class PrjCompanyPageLoad extends DefaultPageHandle {
	/**
	 * 站点数据还原
	 */
	public void contactLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = PrjCompanyUtils.appModule.getPrjColumns("contact");
		Map<String, String> map = PrjMgrUtils.loadCustom("contact");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(map.get(key), ""));
		}
	}

	/**
	 * 站点数据还原
	 */
	public void companyLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		PrjColumns columns = PrjCompanyUtils.appModule.getPrjColumns("company");
		Map<String, String> map = PrjMgrUtils.loadCustom("company");
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(map.get(key), ""));
		}
	}
}
