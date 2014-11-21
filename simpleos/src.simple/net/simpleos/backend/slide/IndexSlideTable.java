package net.simpleos.backend.slide;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;
import net.simpleos.backend.BackendUtils;

/**
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2014-1-20下午05:25:47
 */
public class IndexSlideTable extends AbstractDbTablePagerHandle {
	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		return BackendUtils.applicationModule.queryBean(new ExpressionValue("1=1 order by oorder"), IndexSlideBean.class);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				Map<Object, Object> data = new HashMap<Object, Object>();
				final IndexSlideBean navBean = (IndexSlideBean) dataObject;
				data.put("title", "<a href='" + navBean.getUrl() + "'>" + navBean.getTitle() + "</a>");
				data.put("img", "<img src='" + compParameter.getContextPath() + "/nav/" + navBean.getImage() + "' style='width:300px;height:100px;'>");
				data.put("act", "<a class='nav_b down_menu_image'></a>");
				return data;
			}
		};
	}
}
