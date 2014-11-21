package net.simpleframework.web.page.component.ui.window;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageUtils;
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
public class WindowRender extends AbstractComponentJavascriptRender {
	public WindowRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final WindowBean windowBean = (WindowBean) compParameter.componentBean;

		final String actionFunc = windowBean.getActionFunction();
		final StringBuilder sb = new StringBuilder();
		final boolean destroyOnClose = (Boolean) compParameter.getBeanProperty("destroyOnClose");
		if (!destroyOnClose) {
			sb.append("if (!").append(actionFunc).append(".window) {");
		}

		sb.append(actionFunc).append(".createWindow(");
		final String url = (String) compParameter.getBeanProperty("url");
		if (StringUtils.hasText(url)) {
			sb.append("\"");
			sb.append(
					compParameter.wrapContextPath(PageUtils.doPageUrl(compParameter,
							PageUtils.addParameters(url, "iframe=true")))).append("\", ");
		} else {
			sb.append("null, ");
		}
		final String content = (String) compParameter.getBeanProperty("content");
		if (StringUtils.hasText(content)) {
			sb.append("\"").append(JavascriptUtils.escape(content)).append("\", ");
		} else {
			sb.append("null, ");
		}

		final String contentRef = (String) compParameter.getBeanProperty("contentRef");
		if (StringUtils.hasText(contentRef)) {
			sb.append("\"").append(contentRef).append("\", ");
		} else {
			sb.append("null, ");
		}
		sb.append("arguments[0]);");
		final String jsHiddenCallback = (String) compParameter.getBeanProperty("jsHiddenCallback");
		if (StringUtils.hasText(jsHiddenCallback)) {
			sb.append(actionFunc).append(".observe('hidden', function() {");
			sb.append(jsHiddenCallback).append("});");
		}
		final String jsShownCallback = (String) compParameter.getBeanProperty("jsShownCallback");
		if (StringUtils.hasText(jsShownCallback)) {
			sb.append(actionFunc).append(".observe('shown', function() {");
			sb.append(jsShownCallback).append("});");
		}
		if (!destroyOnClose) {
			sb.append("}");
		}

		sb.append(actionFunc).append(".showWindow(");
		final String title = (String) compParameter.getBeanProperty("title");
		if (StringUtils.hasText(title)) {
			sb.append("\"").append(title).append("\", ");
		} else {
			sb.append("null, ");
		}
		final boolean popup = (Boolean) compParameter.getBeanProperty("popup");
		final boolean showCenter = (Boolean) compParameter.getBeanProperty("showCenter");
		final boolean modal = (Boolean) compParameter.getBeanProperty("modal");
		sb.append(popup).append(", ");
		sb.append(showCenter).append(", ");
		sb.append(modal).append(");");

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("__window_actions_init(").append(actionFunc).append(", '")
				.append(windowBean.getName()).append("', ")
				.append((!modal || popup) && (Boolean) compParameter.getBeanProperty("singleWindow"))
				.append(");");
		sb2.append(jsonOptions(compParameter, actionFunc, popup, destroyOnClose));
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString(), sb2.toString());
	}

	private static String jsonOptions(final ComponentParameter compParameter,
			final String actionFunc, final boolean popup, final boolean destroyOnClose) {
		final StringBuilder sb = new StringBuilder();
		sb.append(actionFunc).append(".options = {");
		if (popup) {
			sb.append("theme: \"popup\",");
		}
		final int top = (Integer) compParameter.getBeanProperty("top");
		if (top > 0) {
			sb.append("top: ").append(top).append(",");
		}
		final int left = (Integer) compParameter.getBeanProperty("left");
		if (left > 0) {
			sb.append("left: ").append(left).append(",");
		}
		sb.append("width: ").append(compParameter.getBeanProperty("width")).append(",");
		sb.append("height: ").append(compParameter.getBeanProperty("height")).append(",");
		sb.append("minWidth: ").append(compParameter.getBeanProperty("minWidth")).append(",");
		sb.append("minHeight: ").append(compParameter.getBeanProperty("minHeight")).append(",");
		final int maxWidth = (Integer) compParameter.getBeanProperty("maxWidth");
		if (maxWidth > 0) {
			sb.append("maxWidth: ").append(maxWidth).append(",");
		}
		final int maxHeight = (Integer) compParameter.getBeanProperty("maxHeight");
		if (maxHeight > 0) {
			sb.append("maxHeight: ").append(maxHeight).append(",");
		}

		if (!(Boolean) compParameter.getBeanProperty("minimize")) {
			sb.append("minimize: false,");
		}
		if (!(Boolean) compParameter.getBeanProperty("maximize")) {
			sb.append("maximize: false,");
		}
		if (!(Boolean) compParameter.getBeanProperty("resizable")) {
			sb.append("resizable: false,");
		}
		if (!(Boolean) compParameter.getBeanProperty("draggable")) {
			sb.append("draggable: false,");
		}
		if (!destroyOnClose) {
			sb.append("close: \"hide\",");
		}
		sb.append("effects : Browser.effects && ").append(compParameter.getBeanProperty("effects"));
		sb.append("};");
		return sb.toString();
	}
}
