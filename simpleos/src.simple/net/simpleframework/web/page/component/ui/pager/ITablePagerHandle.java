package net.simpleframework.web.page.component.ui.pager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.simpleframework.core.ado.db.Column;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.validation.ValidatorBean;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ITablePagerHandle extends IPagerHandle {
	static final String FIRST_ITEM = "__first_item";

	static final String LAST_ITEM = "__last_item";

	List<?> getData2Top(ComponentParameter compParameter);

	AbstractTablePagerData createTablePagerData(ComponentParameter compParameter);

	void export(ComponentParameter compParameter, EExportFileType filetype,
			Map<String, TablePagerColumn> columns);

	// Column

	Column getSortColumn(ComponentParameter compParameter);

	Map<String, Column> getFilterColumns(ComponentParameter compParameter);

	Collection<ValidatorBean> getFilterColumnValidators(ComponentParameter compParameter,
			TablePagerColumn oCol);

	// menu
	List<MenuItem> getHeaderMenu(ComponentParameter compParameter, MenuBean menuBean);
}
