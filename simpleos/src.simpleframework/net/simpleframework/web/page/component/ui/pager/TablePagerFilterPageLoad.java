package net.simpleframework.web.page.component.ui.pager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.simpleframework.core.ado.db.Column;
import net.simpleframework.core.ado.db.EFilterOpe;
import net.simpleframework.core.ado.db.FilterItem;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TablePagerFilterPageLoad extends DefaultPageHandle {

	@Override
	public void pageLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final String col = pageParameter.getRequestParameter(TablePagerUtils.PAGER_FILTER_CUR_COL);
		if (!StringUtils.hasText(col)) {
			return;
		}
		final ComponentParameter nComponentParameter = PagerUtils
				.getComponentParameter(pageParameter);
		boolean disabled = true;
		Column column;
		final Map<String, Column> columns = ((ITablePagerHandle) nComponentParameter
				.getComponentHandle()).getFilterColumns(nComponentParameter);
		if (columns != null && (column = columns.get(col)) != null) {
			final Iterator<FilterItem> it = column.getFilterItems().iterator();
			if (it.hasNext()) {
				FilterItem item = it.next();
				dataBinding.put("tp_filter_r1", item.getRelation().toString());
				dataBinding.put("tp_filter_v1", item.getOvalue());
				final EFilterOpe ope = item.getOpe();
				if (ope == EFilterOpe.and) {
					dataBinding.put("tp_filter_op1", true);
					disabled = false;
				} else if (ope == EFilterOpe.or) {
					dataBinding.put("tp_filter_op2", true);
					disabled = false;
				}
				if (it.hasNext()) {
					item = it.next();
					dataBinding.put("tp_filter_r2", item.getRelation().toString());
					dataBinding.put("tp_filter_v2", item.getOvalue());
				}
			}
		}
		if (disabled) {
			disabledSelector.add("#tp_filter_r2, #tp_filter_v2");
		}
	}
}
