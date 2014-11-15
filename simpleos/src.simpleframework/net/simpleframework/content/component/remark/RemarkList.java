package net.simpleframework.content.component.remark;

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
public class RemarkList extends AbstractPagerHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("containerId".equals(beanProperty)) {
			return "remark_list_" + compParameter.getRequestParameter(RemarkUtils.BEAN_ID);
		} else if ("selector".equals(beanProperty)) {
			final ComponentParameter nComponentParameter = RemarkUtils
					.getComponentParameter(compParameter);
			return nComponentParameter.getBeanProperty("selector");
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public IDataObjectQuery<?> createDataObjectQuery(final ComponentParameter compParameter) {
		final ComponentParameter nComponentParameter = RemarkUtils
				.getComponentParameter(compParameter);
		return ((IRemarkHandle) nComponentParameter.getComponentHandle())
				.getRemarkItems(nComponentParameter);
	}
}
