package net.itsite.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IJob;
import net.simpleframework.sysmgr.dict.DictUtils;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

public class WisdomPaper extends AbstractDbTablePagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("jobView".equals(beanProperty)) {
			return IJob.sj_manager;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final ITableEntityManager tMgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, WisdomBean.class);
		final StringBuffer sql = new StringBuffer();
		int type = ConvertUtils.toInt(DictUtils.getSysDictByName("wisdom.wisdom_v"), 0);
		sql.append("type=?");
		final List<Object> lv = new ArrayList<Object>();
		lv.add(type);
		return tMgr.query(new ExpressionValue(sql.toString(), lv.toArray()), WisdomBean.class);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(Object dataObject) {
				final WisdomBean wisdomBean = (WisdomBean) dataObject;
				final Map<Object, Object> row = new HashMap<Object, Object>();

				row.put("content", wisdomBean.getContent());
				row.put("type", wisdomBean.getType());
				row.put("action", "<a class='dellogB down_menu_image'></a>");
				return row;
			}

		};
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		return parameters;
	}

}
