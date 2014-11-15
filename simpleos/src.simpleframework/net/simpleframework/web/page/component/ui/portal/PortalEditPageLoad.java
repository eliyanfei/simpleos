package net.simpleframework.web.page.component.ui.portal;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.module.IPortalModuleHandle;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalEditPageLoad extends DefaultPageHandle {

	public void optionLoaded(final PageParameter pageParameter,
			final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final IPortalModuleHandle mh = PortalModuleRegistryFactory.getInstance().getModuleHandle(
				PortalUtils.getPageletBean(pageParameter));
		if (mh != null) {
			mh.optionLoaded(pageParameter, dataBinding);
		}
	}

	public void uiOptionLoaded(final PageParameter pageParameter,
			final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final PageletBean pagelet = PortalUtils.getPageletBean(pageParameter);
		if (pagelet == null) {
			return;
		}

		final PageletTitle title = pagelet.getTitle();
		if (title != null) {
			dataBinding.put("ui_options_title", title.getValue());
			dataBinding.put("ui_options_link", title.getLink());
			final String icon = title.getIcon();
			if (StringUtils.hasText(icon)) {
				dataBinding.put("__homepath", pagelet.getColumnBean().getPortalBean()
						.getResourceHomePath(pageParameter)
						+ "/jsp/icons/");
				dataBinding.put("ui_options_icon", icon);
			}
			final String f = title.getFontStyle();
			if (StringUtils.hasText(f)) {
				dataBinding.put("ui_options_fontstyle", f);
			}
			dataBinding.put("ui_options_desc", title.getDescription());
		}

		final int height = pagelet.getHeight();
		if (height > 0) {
			dataBinding.put("ui_options_height", height);
		}
		dataBinding.put("ui_options_align", pagelet.getAlign());
		final String fontStyle = pagelet.getFontStyle();
		if (StringUtils.hasText(fontStyle)) {
			dataBinding.put("ui_options_c_fontstyle", fontStyle);
		}

		dataBinding.put("ui_options_sync", pagelet.isSync());
	}

	public void columnSizeLoaded(final PageParameter pageParameter,
			final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = PortalUtils
				.getComponentParameter(pageParameter);
		if (nComponentParameter.componentBean == null) {
			return;
		}
		final Collection<ColumnBean> columns = PortalUtils.getColumns(nComponentParameter);
		dataBinding.put("_columns_select", columns.size());
		int i = 1;
		for (final ColumnBean column : columns) {
			dataBinding.put("_cw" + i++, column.getWidth());
		}
	}
}
