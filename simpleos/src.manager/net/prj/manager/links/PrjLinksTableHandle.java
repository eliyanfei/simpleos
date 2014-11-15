package net.prj.manager.links;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 
 * @Description：
 * @author: 李岩飞
 * @Time: Apr 1, 2011 7:25:46 PM
 */
public class PrjLinksTableHandle extends AbstractDbTablePagerHandle {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		return PrjLinksUtils.appModule.queryBean("1=1 order by oorder", new Object[] {}, PrjLinksBean.class);
	}

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				PrjLinksBean linksBean = (PrjLinksBean) dataObject;
				Map<Object, Object> rowData = new HashMap<Object, Object>();
				rowData.putAll(BeanUtils.toMap(linksBean, true));
				rowData.put("title", "<a style='color:" + linksBean.getColor() + ";' href='" + linksBean.getUrl() + "'>" + linksBean.getTitle()
						+ "</a>");
				rowData.put("startDate", ConvertUtils.toDateString(linksBean.getCreatedDate(), "yyyy-MM-dd"));
				String href = "<a class=\"links_menu down_menu_image\" ></a>";
				rowData.put("action", href);
				return rowData;
			}

		};
	}
}
