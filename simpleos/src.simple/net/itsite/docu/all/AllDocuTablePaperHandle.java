package net.itsite.docu.all;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.itsite.docu.AbstractDocuTablePagerData;
import net.itsite.docu.DocuBean;
import net.itsite.docu.DocuCatalog;
import net.itsite.docu.DocuUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.ContentUtils;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;

public class AllDocuTablePaperHandle extends AbstractDbTablePagerHandle {
	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_manager;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager();
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		final StringBuffer order = new StringBuffer();
		sql.append("1=1");
		if (!ItSiteUtil.isManage(compParameter)) {
			sql.append(" and status=?");
			ol.add(EContentStatus.publish);
		}
		String status = compParameter.getParameter("status");
		try {
			ol.add(EContentStatus.valueOf(status));
			sql.append(" and status=?");
		} catch (Exception e) {
		}
		
		if (!ItSiteUtil.isManage(compParameter, DocuUtils.applicationModule)) {
			sql.append(" and userId=?");
			ol.add(ItSiteUtil.getLoginUser(compParameter).getId());
		}
		order.append("status ,createDate desc");
		sql.append(" order by ").append(order);
		return tMgr.query(new ExpressionValue(sql.toString(), ol.toArray(new Object[] {})), DocuBean.class);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractDocuTablePagerData(compParameter) {

			@Override
			protected Map<Object, Object> getRowData(final Object arg0) {
				final DocuBean docuBean = (DocuBean) arg0;
				final Map<Object, Object> rowData = new LinkedHashMap<Object, Object>();
				rowData.put("title", buildTitle(docuBean));
				rowData.put("userId", ContentUtils.getAccountAware().wrapAccountHref(compParameter, docuBean.getUserId(), docuBean.getUserText()));
				final DocuCatalog catalog = DocuUtils.applicationModule.getBean(DocuCatalog.class, docuBean.getCatalogId());
				rowData.put("catalogId", catalog == null ? "" : catalog.getText());
				rowData.put("createDate", ConvertUtils.toDateString(docuBean.getCreateDate(), "yyyy-MM-dd HH"));
				rowData.put("action", "<a class='allDB down_menu_image'></a>");
				return rowData;
			}
		};
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "stauts");
		return parameters;
	}

}
