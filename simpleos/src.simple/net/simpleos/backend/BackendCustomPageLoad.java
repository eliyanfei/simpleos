package net.simpleos.backend;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleos.SimpleosUtil;
import net.simpleos.impl.PrjColumns;
import net.simpleos.utils.StringsUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午04:32:04
 */
public class BackendCustomPageLoad extends DefaultPageHandle {
	/**
	 * simpleos_keyvalue 数据源还原
	 */
	public void keyvalueLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		final String kv_name = pageParameter.getRequestParameter("kv_name");
		KeyValueBean keyValueBean = BackendUtils.applicationModule.getBeanByExp(KeyValueBean.class, "name=?", new Object[] { kv_name });
		if (keyValueBean != null) {
			dataBinding.put("kv_name", keyValueBean.getName());
			dataBinding.put("kv_content", keyValueBean.getContent());
		} else {
			dataBinding.put("kv_name", kv_name);
		}
	}

	/**
	 * simpleos_custom 数据源还原
	 */
	public void dataLoad(PageParameter pageParameter, Map<String, Object> dataBinding, List<String> visibleToggleSelector,
			List<String> readonlySelector, List<String> disabledSelector) {
		final String type = pageParameter.getRequestParameter("type");
		PrjColumns columns = BackendUtils.applicationModule.getPrjColumns(type);
		for (final String key : columns.getColumnMap().keySet()) {
			dataBinding.put(key, StringsUtils.trimNull(SimpleosUtil.attrMap.get(type + "." + key), ""));
		}
	}

}
