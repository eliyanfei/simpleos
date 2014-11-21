package net.simpleframework.web.page.component.ui.colorpalette;

import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentHtmlRender;
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
public class ColorPaletteRender extends AbstractComponentHtmlRender {
	public ColorPaletteRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	protected String getRelativePath(final ComponentParameter compParameter) {
		final StringBuilder sb = new StringBuilder();
		sb.append("/jsp/colorpalette.jsp?").append(ColorPaletteUtils.BEAN_ID);
		sb.append("=").append(compParameter.componentBean.hashId());
		return sb.toString();
	}

	public String jsColorpicker(final ComponentParameter compParameter) {
		final ColorPaletteBean colorPalette = (ColorPaletteBean) compParameter.componentBean;

		final StringBuilder sb = new StringBuilder();
		final String actionFunc = colorPalette.getActionFunction();
		sb.append("var ").append(actionFunc).append(" = $Actions[\"");
		sb.append(compParameter.getBeanProperty("name")).append("\"];");

		sb.append(actionFunc).append(".options = Object.extend({");
		final String changeCallback = colorPalette.getChangeCallback();
		if (StringUtils.hasText(changeCallback)) {
			sb.append("onValuesChanged: function(picker) {");
			sb.append("var callback = function(value) {");
			sb.append(changeCallback).append("};");
			sb.append("callback(picker.color.hex);");
			sb.append("},");
		}
		sb.append("startMode:\"");
		sb.append(colorPalette.getStartMode()).append("\",");
		sb.append("startHex:\"");
		sb.append(colorPalette.getStartHex()).append("\",");
		sb.append(ComponentRenderUtils.jsonHeightWidth(compParameter));
		sb.append("clientFilesPath:\"").append(getCssResourceHomePath(compParameter))
				.append("/images/\"");
		sb.append("}, ").append(actionFunc).append(".options);");

		sb.append(ComponentRenderUtils.initContainerVar(compParameter));
		sb.append("var cId = ").append(ComponentRenderUtils.VAR_CONTAINER).append(".identify();");
		sb.append("c.select(\"INPUT, .map, .bar, .preview\").each(function(o) {");
		sb.append("var a1 = o.getAttribute(\"id\");");
		sb.append("if (a1) { o.setAttribute(\"id\", cId + a1); }");
		sb.append("var a2 = o.getAttribute(\"name\");");
		sb.append("if (a2) { o.setAttribute(\"name\", cId + a2); }");
		sb.append("});");
		sb.append(actionFunc).append(".colorpicker = new $Comp.ColorPicker(cId, ");
		sb.append(actionFunc).append(".options);");
		return JavascriptUtils.wrapWhenReady(sb.toString());
	}
}
