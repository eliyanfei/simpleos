package net.simpleframework.applets.attention;

import java.util.Map;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.content.IContentPagerHandle;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttentionUsersPagerHandle extends AbstractPagerHandle {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "attentionId");
		putParameter(compParameter, parameters, IContentPagerHandle._VTYPE);
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final String attentionId = compParameter.getRequestParameter("attentionId");
		if (StringUtils.hasText(attentionId)) {
			final EFunctionModule vtype = ConvertUtils.toEnum(EFunctionModule.class,
					compParameter.getRequestParameter(IContentPagerHandle._VTYPE));
			return AttentionUtils.getTableEntityManager()
					.query(
							new ExpressionValue("attentionid=? and vtype=?", new Object[] { attentionId,
									vtype }), AttentionBean.class);
		} else {
			return null;
		}
	}
}
