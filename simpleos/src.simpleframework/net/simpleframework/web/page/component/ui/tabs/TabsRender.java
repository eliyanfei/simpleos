package net.simpleframework.web.page.component.ui.tabs;

import java.util.Collection;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.LocaleI18n;
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
public class TabsRender extends AbstractComponentJavascriptRender {
	public TabsRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final TabsBean tabsBean = (TabsBean) compParameter.componentBean;

		Collection<TabItem> tabs = null;
		final ITabsHandle tabsHandle = (ITabsHandle) compParameter.getComponentHandle();
		if (tabsHandle != null) {
			tabs = tabsHandle.getTabItems(compParameter);
		}
		if (tabs == null) {
			tabs = tabsBean.getTabItems();
		}

		final String actionFunc = tabsBean.getActionFunction();
		final StringBuilder sb = new StringBuilder();
		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		sb.append(actionFunc).append(".options = {");
		sb.append("effects: Browser.effects && ").append(compParameter.getBeanProperty("effects"))
				.append(",");
		final String parameters = tabsBean.getParameters();
		if (StringUtils.hasText(parameters)) {
			sb.append("parameters: \"").append(parameters).append("\".addParameter(arguments[0]),");
		} else {
			sb.append("parameters: arguments[0],");
		}
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		sb.append("activeIndex: ").append(compParameter.getBeanProperty("activeIndex"));
		sb.append("};");

		sb.append(actionFunc).append(".tabs = new $Comp.Tabs(");
		sb.append(ComponentRenderUtils.VAR_CONTAINER).append(", [");
		if (tabs != null) {
			int i = 0;
			for (final TabItem tab : tabs) {
				if (i++ > 0) {
					sb.append(",");
				}
				sb.append("{");
				final String content = tab.getContent();
				if (StringUtils.hasText(content)) {
					sb.append("content: \"");
					sb.append(JavascriptUtils.escape(LocaleI18n.replaceI18n(content))).append("\",");
				}
				final String contentStyle = tab.getContentStyle();
				if (StringUtils.hasText(contentStyle)) {
					sb.append("contentStyle: \"").append(contentStyle).append("\",");
				}
				final String contentRef = tab.getContentRef();
				if (StringUtils.hasText(contentRef)) {
					sb.append("contentRef: \"").append(contentRef).append("\",");
				}
				sb.append("cache: ").append(tab.isCache()).append(",");
				final String jsActiveCallback = tab.getJsActiveCallback();
				if (StringUtils.hasText(jsActiveCallback)) {
					sb.append("onActive: \"");
					sb.append(JavascriptUtils.escape(jsActiveCallback)).append("\",");
				}
				final String jsContentLoadedCallback = tab.getJsContentLoadedCallback();
				if (StringUtils.hasText(jsContentLoadedCallback)) {
					sb.append("onContentLoaded: \"");
					sb.append(JavascriptUtils.escape(jsContentLoadedCallback)).append("\",");
				}
				sb.append("title: \"");
				sb.append(JavascriptUtils.escape(tab.getTitle())).append("\"");
				sb.append("}");
			}
		}
		sb.append("], ").append(actionFunc).append(".options);");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString());
	}
}