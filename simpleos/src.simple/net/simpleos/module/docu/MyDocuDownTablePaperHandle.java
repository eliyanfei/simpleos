package net.simpleos.module.docu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.organization.IJob;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleos.SimpleosUtil;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午5:15:27 
 *
 */
public class MyDocuDownTablePaperHandle extends AllDocuDownTablePaperHandle {
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
		sql.append(" userId=?");
		ol.add(SimpleosUtil.getLoginUser(compParameter).getId());
		sql.append(" order by downDate desc");
		return tMgr.query(new ExpressionValue(sql.toString(), ol.toArray(new Object[] {})), DocuLogBean.class);
	}

	@Override
	public Map<String, Object> getFormParameters(ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "userId");
		putParameter(compParameter, parameters, "t");
		return parameters;
	}

}
