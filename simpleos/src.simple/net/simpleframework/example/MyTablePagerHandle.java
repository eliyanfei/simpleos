package net.simpleframework.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.ListDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerHandle;
import net.simpleframework.web.page.component.ui.pager.GroupTablePagerBean;

public class MyTablePagerHandle extends AbstractTablePagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("title".equals(beanProperty)
				&& !(compParameter.componentBean instanceof GroupTablePagerBean)) {
			final StringBuilder sb = new StringBuilder();
			if (ConvertUtils.toBoolean(compParameter.getRequestParameter("t200"), false)) {
				sb.append("<a onclick=\"$Actions['demoTablePager']('t200=false');\">").append("切换内存数据")
						.append("</a>");
			} else {
				sb.append("<a onclick=\"$Actions['demoTablePager']('t200=true');\">");
				sb.append("切换数据库数据(200万)").append("</a>");
				sb.append(HTMLBuilder.SEP);
				sb.append("<a onclick=\"$Actions['").append(compParameter.componentBean.getName())
						.append("'].exportFile(null, false);\">导出</a>");
			}
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "t200");
		return parameters;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		if (ConvertUtils.toBoolean(compParameter.getRequestParameter("t200"), false)) {
			return DataObjectManagerUtils.getTableEntityManager(WebUtils.application,
					new Table("test")).query(null);
		} else {
			final List data = new ArrayList();
			final java.util.Random r = new java.util.Random();
			for (int i = 0; i < 1000; i++) {
				final Map row = new HashMap();
				row.put("t1", "分组字段（Group By） " + r.nextInt(6));
				row.put("t2", "k2_" + i);
				row.put("t3", "k3_" + i);
				row.put("t4", "测试_" + i);
				data.add(row);
			}
			return new ListDataObjectQuery(data);
		}
	}
}
