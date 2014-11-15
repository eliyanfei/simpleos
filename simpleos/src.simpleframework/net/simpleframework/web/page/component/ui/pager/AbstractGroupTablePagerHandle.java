package net.simpleframework.web.page.component.ui.pager;

import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractGroupTablePagerHandle extends AbstractTablePagerHandle implements
		IGroupTablePagerHandle {

	@Override
	public GroupWrapper getGroupWrapper(final ComponentParameter compParameter,
			final String groupValue) {
		return null;
	}
}
