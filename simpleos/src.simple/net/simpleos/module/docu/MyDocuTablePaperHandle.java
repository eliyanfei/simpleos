package net.simpleos.module.docu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.my.space.MySpaceUtils;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IGetAccountAware;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;
import net.simpleframework.web.page.component.ui.pager.db.AbstractDbTablePagerHandle;
import net.simpleos.SimpleosUtil;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午5:41:01 
 *
 */
public class MyDocuTablePaperHandle extends AbstractDbTablePagerHandle {
	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager tMgr = DocuUtils.applicationModule.getDataObjectManager();
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		final StringBuffer order = new StringBuffer();
		sql.append("status=?");
		ol.add(EDocuStatus.publish);
		sql.append(" and userId=?");
		final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
		if (accountAware.isMyAccount(compParameter)) {
			ol.add(SimpleosUtil.getLoginUser(compParameter).getId());
		} else {
			ol.add(compParameter.getRequestParameter(OrgUtils.um().getUserIdParameterName()));
		}
		order.append("createDate desc");
		sql.append(" order by ").append(order);
		return tMgr.query(new ExpressionValue(sql.toString(), ol.toArray(new Object[] {})), DocuBean.class);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new AbstractDocuTablePagerData(compParameter) {
			@Override
			public Map<String, TablePagerColumn> getTablePagerColumns() {
				final Map<String, TablePagerColumn> column = new LinkedHashMap<String, TablePagerColumn>(super.getTablePagerColumns());
				final IGetAccountAware accountAware = MySpaceUtils.getAccountAware();
				if (!accountAware.isMyAccount(compParameter) && !SimpleosUtil.isManage(compParameter, DocuUtils.applicationModule)) {
					column.remove("action");
				}
				return column;
			}

			@Override
			protected Map<Object, Object> getRowData(final Object arg0) {
				final DocuBean docuBean = (DocuBean) arg0;
				final Map<Object, Object> rowData = new LinkedHashMap<Object, Object>();
				rowData.put("title", buildTitle(docuBean));
				final DocuCatalog catalog = DocuUtils.applicationModule.getBean(DocuCatalog.class, docuBean.getCatalogId());
				rowData.put("catalogId", catalog == null ? "" : catalog.getText());
				rowData.put("createDate", ConvertUtils.toDateString(docuBean.getCreateDate(), "yyyy-MM-dd HH"));
				rowData.put("action", "<a class='myDB1 down_menu_image'></a>");
				return rowData;
			}
		};
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "docu_type");
		putParameter(compParameter, parameters, "docuFunction");
		putParameter(compParameter, parameters, OrgUtils.um().getUserIdParameterName());
		return parameters;
	}

}
