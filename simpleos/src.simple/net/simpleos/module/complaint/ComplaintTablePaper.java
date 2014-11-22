package net.simpleos.module.complaint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IJob;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;
import net.simpleos.SimpleosUtil;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月22日 上午11:37:15 
 *
 */
public class ComplaintTablePaper extends AbstractPagerHandle {
	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobExecute".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager tMgr = ComplaintAppModule.applicationModule.getDataObjectManager();
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		sql.append("1=1");
		if (!SimpleosUtil.isManage(compParameter, ComplaintAppModule.applicationModule)) {
			ol.add(SimpleosUtil.getLoginUser(compParameter).getId());
			sql.append(" and userId=?");
		}
		final String deal = compParameter.getRequestParameter("deal");
		if ("1".equals(deal)) {
			sql.append(" and deal=?");
			ol.add(true);
		} else if ("0".equals(deal)) {
			sql.append(" and deal=?");
			ol.add(false);
		}
		sql.append(" order by createDate desc");
		return tMgr.query(new ExpressionValue(sql.toString(), ol.toArray(new Object[] {})), ComplaintBean.class);
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "deal");
		return parameters;
	}

}
