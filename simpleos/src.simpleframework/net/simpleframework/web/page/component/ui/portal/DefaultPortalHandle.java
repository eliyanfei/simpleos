package net.simpleframework.web.page.component.ui.portal;

import java.io.IOException;
import java.util.Collection;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultPortalHandle extends AbstractPortalHandle {
	@Override
	public Collection<ColumnBean> getPortalColumns(final ComponentParameter compParameter) {
		return ((PortalBean) compParameter.componentBean).getColumns();
	}

	@Override
	public void updatePortal(final ComponentParameter compParameter,
			final Collection<ColumnBean> columns, final boolean draggable) {
		final PortalBean portalBean = (PortalBean) compParameter.componentBean;
		portalBean.setDraggable(draggable);
		if (columns != null) {
			final Collection<ColumnBean> _columns = portalBean.getColumns();
			if (!_columns.equals(columns)) {
				_columns.clear();
				_columns.addAll(columns);
			}
		}
		try {
			portalBean.saveToFile();
		} catch (final IOException e) {
			throw HandleException.wrapException(e);
		}
	}
}
