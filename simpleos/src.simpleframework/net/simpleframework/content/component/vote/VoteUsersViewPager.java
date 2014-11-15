package net.simpleframework.content.component.vote;

import java.util.Map;

import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.pager.AbstractPagerHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VoteUsersViewPager extends AbstractPagerHandle {

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter compParameter) {
		final Map<String, Object> parameters = super.getFormParameters(compParameter);
		putParameter(compParameter, parameters, "itemId");
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		parameters.putAll(vHandle.getFormParameters(nComponentParameter));
		return parameters;
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = VoteUtils.getComponentParameter(compParameter);
		final IVoteHandle vHandle = (IVoteHandle) nComponentParameter.getComponentHandle();
		return vHandle.getResults(nComponentParameter, compParameter.getRequestParameter("itemId"));
	}
}
