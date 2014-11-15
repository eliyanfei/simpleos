package net.itsite.common;

import java.util.Map;

import net.simpleframework.applets.attention.AttentionUtils;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.LongID;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.EFunctionModule;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

public class AttentionUsersPagerHandle extends AbstractPagerHandle {
	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "attentionId");
		putParameter(compParameter, parameters, "vtype");
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final String attentionId = compParameter.getRequestParameter("attentionId");
		final String vtype = compParameter.getRequestParameter("vtype");
		if (StringUtils.hasText(attentionId)) {
			return AttentionUtils.queryAttentions(EFunctionModule.valueOf(vtype), new LongID(attentionId));
		} else {
			return null;
		}
	}
}
