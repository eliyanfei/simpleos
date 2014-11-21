package net.simpleframework.web.page.component;

import java.util.Map;

import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.JSONUtils;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class ComponentRenderUtils {

	public static String VAR_CONTAINER = "c";

	public static String actionFunc(final ComponentParameter cp) {
		return "f_" + cp.componentBean.hashId();
	}

	public static String initContainerVar(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = compParameter.componentBean.getActionFunction();
		sb.append("var ").append(VAR_CONTAINER).append("=").append(actionFunc).append(".container");
		final String containerId = (String) compParameter.getBeanProperty("containerId");
		if (StringUtils.hasText(containerId)) {
			sb.append(" || $(\"").append(containerId).append("\")");
		}
		sb.append("; if (!").append(VAR_CONTAINER).append(") return;");
		return sb.toString();
	}

	public static String initHeightWidth(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append(VAR_CONTAINER).append(".setDimensions({");
		final String hw = jsonHeightWidth(compParameter);
		if (StringUtils.hasText(hw)) {
			sb.append(hw);
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("});");
		return sb.toString();
	}

	public static String jsonHeightWidth(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final String height = (String) compParameter.getBeanProperty("height");
		if (StringUtils.hasText(height)) {
			sb.append("\"height\": \"").append(height).append("\",");
		}
		final String width = (String) compParameter.getBeanProperty("width");
		if (StringUtils.hasText(width)) {
			sb.append("\"width\": \"").append(width).append("\",");
		}
		return sb.toString();
	}

	public static String wrapActionFunction(final ComponentParameter compParameter, final String wrapCode) {
		return wrapActionFunction(compParameter, wrapCode, null);
	}

	public static String wrapActionFunction(final ComponentParameter compParameter, final String wrapCode, final String execCode) {
		return wrapActionFunction(compParameter, wrapCode, execCode, (Boolean) compParameter.getBeanProperty("runImmediately"));
	}

	public static String wrapActionFunction(final ComponentParameter compParameter, final String wrapCode, final String execCode,
			final boolean runImmediately) {
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = compParameter.componentBean.getActionFunction();
		sb.append("var ").append(actionFunc).append("=function() {");
		sb.append(StringUtils.blank(wrapCode));
		sb.append("return true;");
		sb.append("};");
		final IComponentHandle handle = compParameter.getComponentHandle();
		if (handle != null) {
			final Map<String, Object> json = handle.toJSON(compParameter);
			if (json != null) {
				sb.append(actionFunc).append(".json=");
				sb.append(JSONUtils.toJSON(json)).append(";");
			}
		}
		sb.append(JavascriptUtils.wrapFunction(execCode));
		sb.append("$Actions[\"").append(compParameter.getBeanProperty("name")).append("\"]=").append(actionFunc).append(";");
		if (runImmediately) {
			sb.append(JavascriptUtils.wrapWhenReady(actionFunc + "();"));
		}
		return sb.toString();
	}

	public static String genParameters(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		final Map<String, Object> params = IComponentHandle.Utils.toFormParameters(compParameter);
		if (params != null) {
			sb.append("<form id=\"").append(AbstractComponentBean.FORM_PREFIX);
			sb.append(compParameter.componentBean.hashId()).append("\">");
			for (final Map.Entry<String, Object> entry : params.entrySet()) {
				sb.append(HTMLBuilder.inputHidden(entry.getKey(), StringUtils.blank(entry.getValue())));
			}
			sb.append("</form>");
		}
		return sb.toString();
	}
}
