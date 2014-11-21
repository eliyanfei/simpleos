package net.simpleframework.web.page.component.ui.menu;

import java.util.Collection;

import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MenuRender extends AbstractComponentJavascriptRender {
	public MenuRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final MenuBean menuBean = (MenuBean) compParameter.componentBean;
		menuBean.parseElement();
		Collection<MenuItem> menuItems = null;
		final IMenuHandle mHandle = (IMenuHandle) compParameter.getComponentHandle();
		if (mHandle != null) {
			menuItems = mHandle.getMenuItems(compParameter, null);
		}
		if (menuItems == null) {
			menuItems = menuBean.getMenuItems();
		}
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = menuBean.getActionFunction();
		final String containerId = (String) compParameter.getBeanProperty("containerId");
		final boolean container = StringUtils.hasText(containerId);
		if (container) {
			sb.append(ComponentRenderUtils.initContainerVar(compParameter));
			sb.append(actionFunc).append(".menu = new $Comp.Menu(")
					.append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
			sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		} else {
			sb.append(actionFunc).append(".menu = new $Comp.Menu(null, {");
			final String selector = (String) compParameter.getBeanProperty("selector");
			if (StringUtils.hasText(selector)) {
				sb.append("\"selector\": \"").append(selector).append("\",");
			}
			final EMenuEvent menuEvent = (EMenuEvent) compParameter.getBeanProperty("menuEvent");
			if (menuEvent != null) {
				sb.append("\"menuEvent\": \"").append(menuEvent).append("\",");
			}
		}

		final int minWidth = ConvertUtils.toInt(compParameter.getBeanProperty("minWidth"), 0);
		if (minWidth > 0) {
			sb.append("\"minWidth\": \"").append(minWidth).append("px\",");
		}
		sb.append("\"effects\": Browser.effects && ")
				.append(compParameter.getBeanProperty("effects")).append(",");

		final String beforeShowCallback = (String) compParameter
				.getBeanProperty("jsBeforeShowCallback");
		if (StringUtils.hasText(beforeShowCallback)) {
			sb.append("\"onBeforeShow\": function(menu, e) {");
			sb.append(beforeShowCallback);
			sb.append("},");
		}
		sb.append("\"menuItems\": ").append(jsonData(compParameter, menuItems));
		sb.append("});");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString(),
				"__menu_actions_init(" + actionFunc + ");");
	}

	String jsonData(final ComponentParameter compParameter, final Collection<MenuItem> menuItems) {
		final String iconPath = (String) compParameter.getBeanProperty("iconPath");
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		for (final MenuItem menu : menuItems) {
			if (i++ > 0) {
				sb.append(",");
			}
			sb.append("{");
			final String title = menu.getTitle();
			if (title.equals("-")) {
				sb.append("\"separator\": true");
			} else {
				sb.append("\"name\": \"").append(JavascriptUtils.escape(title)).append("\",");
				final boolean checkbox = menu.isCheckbox();
				if (checkbox) {
					sb.append("\"checkbox\": true,");
					sb.append("\"checked\": ").append(menu.isChecked()).append(",");
					final String checkCallback = menu.getJsCheckCallback();
					if (StringUtils.hasText(checkCallback)) {
						sb.append("\"onCheck\": function(item, e) {");
						sb.append(checkCallback);
						sb.append("},");
					}
				} else {
					final String icon = menu.getIcon();
					if (StringUtils.hasText(icon)) {
						sb.append("\"icon\": \"");
						if (icon.charAt(0) == '#') {
							sb.append(getCssResourceHomePath(compParameter));
							sb.append("/images/icons/");
							sb.append(icon.substring(1)).append(".png");
						} else {
							sb.append(compParameter.wrapContextPath(StringUtils.blank(iconPath) + icon));
						}
						sb.append("\",");
					}
				}
				final String url = menu.getUrl();
				if (StringUtils.hasText(url)) {
					sb.append("\"url\": \"").append(compParameter.wrapContextPath(url)).append("\",");
				} else {
					final String selectCallback = menu.getJsSelectCallback();
					if (StringUtils.hasText(selectCallback)) {
						sb.append("\"onSelect\": function(item, e) {");
						sb.append(selectCallback.trim());
						sb.append("},");
					}
				}

				final String desc = menu.getDescription();
				if (StringUtils.hasText(desc)) {
					sb.append("\"desc\": \"").append(JavascriptUtils.escape(desc)).append("\",");
				}

				Collection<MenuItem> children = null;
				final IMenuHandle handle = (IMenuHandle) compParameter.getComponentHandle();
				if (handle != null) {
					children = handle.getMenuItems(compParameter, menu);
				}
				if (children == null) {
					children = menu.getChildren();
				}
				if (children.size() > 0) {
					sb.append("\"submenu\": ").append(jsonData(compParameter, children)).append(",");
				}
				sb.append("\"disabled\": ").append(menu.isDisabled());
			}
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}
}
