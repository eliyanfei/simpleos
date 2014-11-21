package net.itsite.docu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.IPAndCity;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午5:15:27 
 *
 */
public class AllDocuDownTablePaperHandle extends AbstractDbTablePagerHandle {
	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(ComponentParameter compParameter) {
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager(DocuLogBean.class);
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		sql.append("1=1");
		sql.append(" order by downDate desc");
		return tMgr.query(new ExpressionValue(sql.toString(), ol.toArray(new Object[] {})), DocuLogBean.class);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(final Object arg0) {
				final DocuLogBean logBean = (DocuLogBean) arg0;
				final DocuBean docuBean = DocuUtils.applicationModule.getBean(DocuBean.class, logBean.getDocuId());
				final Map<Object, Object> rowData = new LinkedHashMap<Object, Object>();
				if (docuBean == null)
					return null;
				rowData.put("userId", ContentUtils.getAccountAware().wrapAccountHref(compParameter, logBean.getUserId(), logBean.getUserText()));
				rowData.put("ip", StringUtils.text(logBean.getIp()));
				try {
					rowData.put("ipName", IPAndCity.getCity(StringUtils.text(logBean.getIp()), false));
				} catch (Exception e) {
				}
				rowData.put("title", DocuUtils.wrapOpenLink(compParameter, docuBean));
				rowData.put("downDate", ConvertUtils.toDateString(logBean.getDownDate(), "yyyy-MM-dd HH:mm"));
				rowData.put("action", "<a class='downBody down_menu_image'></a>");
				return rowData;
			}
		};
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "userId");
		putParameter(compParameter, parameters, "t");
		return parameters;
	}

}
