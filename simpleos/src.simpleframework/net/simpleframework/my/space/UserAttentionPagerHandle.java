package net.simpleframework.my.space;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserAttentionPagerHandle extends AbstractPagerHandle {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "userId");
		putParameter(compParameter, parameters, "attentionId");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ITableEntityManager attention_mgr = MySpaceUtils
				.getTableEntityManager(UserAttentionBean.class);
		String userId, attentionId;
		if (StringUtils.hasText(userId = compParameter.getRequestParameter("userId"))) {
			return attention_mgr.query(new ExpressionValue("userid=?", new Object[] { userId }),
					UserAttentionBean.class);
		} else if (StringUtils
				.hasText(attentionId = compParameter.getRequestParameter("attentionId"))) {
			return attention_mgr.query(new ExpressionValue("attentionid=?",
					new Object[] { attentionId }), UserAttentionBean.class);
		}
		return null;
	}
}
