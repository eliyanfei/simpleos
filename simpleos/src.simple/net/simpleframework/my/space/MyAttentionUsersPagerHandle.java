package net.simpleframework.my.space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.itsite.ItSiteUtil;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class MyAttentionUsersPagerHandle extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("title".equals(beanProperty)) {
			final StringBuilder sb = new StringBuilder();
			return sb.toString();
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "userid");
		putParameter(compParameter, parameters, "t");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager attention_mgr = MySpaceUtils.getTableEntityManager(UserAttentionBean.class);
		String userId = compParameter.getRequestParameter("userid");
		final String t = compParameter.getRequestParameter("t");
		final List<Object> ol = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		if (!StringUtils.hasText(userId)) {
			userId = ItSiteUtil.getLoginUser(compParameter).getId().toString();
		}
		if ("fans".equals(t)) {
			sql.append("attentionId=?");
		} else {
			sql.append("userId=?");
		}
		ol.add(userId);
		return attention_mgr.query(new ExpressionValue(sql.toString(), ol.toArray()), UserAttentionBean.class);
	}
}
