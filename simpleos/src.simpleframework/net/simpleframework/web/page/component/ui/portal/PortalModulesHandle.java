package net.simpleframework.web.page.component.ui.portal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.portal.module.PortalModule;
import net.simpleframework.web.page.component.ui.portal.module.PortalModuleRegistryFactory;
import net.simpleframework.web.page.component.ui.tabs.AbstractTabsHandle;
import net.simpleframework.web.page.component.ui.tabs.TabItem;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalModulesHandle extends AbstractTabsHandle {

	@Override
	public Collection<TabItem> getTabItems(final ComponentParameter compParameter) {
		final ArrayList<TabItem> al = new ArrayList<TabItem>();
		final Map<String, Collection<PortalModule>> modules = PortalModuleRegistryFactory
				.getInstance().getModulesByCatalog();
		for (final Map.Entry<String, Collection<PortalModule>> entry : modules.entrySet()) {
			final TabItem item = new TabItem();
			al.add(item);
			item.setTitle(entry.getKey());
			final StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"lm\">");
			for (final PortalModule module : entry.getValue()) {
				sb.append("<div class=\"module\"><table style=\"width: 100%;\"><tr>");
				sb.append("<td class=\"icon\"><img src=\"");
				sb.append(compParameter.wrapContextPath(module.getIcon()));
				sb.append("\" /></td>");
				sb.append("<td><div class=\"act\">").append(module.getText()).append("<a onclick=\"")
						.append("__portal_add_module('").append(module.getName()).append("');")
						.append("\">").append(LocaleI18n.getMessage("Add")).append("</a></div>");
				sb.append("<div class=\"md\">").append(StringUtils.blank(module.getDescription()))
						.append("</div></td>");
				sb.append("</tr></table></div>");
			}
			sb.append("</div>");
			item.setContent(sb.toString());
		}
		return al;
	}
}
