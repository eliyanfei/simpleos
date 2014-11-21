package net.itsite;

import java.util.Map;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;
/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:57:24 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class ItSiteTablePagerData extends AbstractTablePagerData {

	protected ItSiteTablePagerData(ComponentParameter compParameter) {
		super(compParameter);
	}

	@Override
	protected Map<Object, Object> getRowData(Object paramObject) {
		return null;
	}

	public Map<Object, Object> getRowData_(Object paramObject) throws Exception {
		return this.getRowData(paramObject);
	}

	public Map<String, TablePagerColumn> getTablePagerColumns_() throws Exception {
		return super.getTablePagerColumns();
	}

	public Map<Object, Object> getRowAttributes_(Object dataObject) throws Exception {
		return super.getRowAttributes(dataObject);
	}
}
