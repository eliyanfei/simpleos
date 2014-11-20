package net.simpleframework.web.page.component.ui.htmleditor;

import java.util.Locale;

import net.itsite.utils.JsonUtils;
import net.simpleframework.util.I18n;
import net.simpleframework.util.JavascriptUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.AbstractComponentJavascriptRender;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ComponentRenderUtils;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class HtmlEditorRender extends AbstractComponentJavascriptRender {
	public HtmlEditorRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter cp) {
		final HtmlEditorBean htmlEditor = (HtmlEditorBean) cp.componentBean;
		final StringBuilder sb = new StringBuilder();
		final String actionFunc = htmlEditor.getActionFunction();
		final String textarea = (String) cp.getBeanProperty("textarea");
		final boolean hasTextarea = StringUtils.hasText(textarea);
		sb.append("if (CKEDITOR._loading) return; CKEDITOR._loading = true;");
		if (hasTextarea) {
			sb.append(actionFunc).append(".editor = CKEDITOR.replace(\"");
			sb.append(textarea).append("\", {");
		} else {
			sb.append(ComponentRenderUtils.initContainerVar(cp));
			sb.append(actionFunc).append(".editor = CKEDITOR.appendTo(").append(ComponentRenderUtils.VAR_CONTAINER).append(", {");
		}
		String homePath = htmlEditor.getComponentRegistry().getComponentResourceProvider().getResourceHomePath(cp);
		sb.append("contentsCss: [\"").append(homePath).append("/css/default").append("/contents.css").append("\"],");
		sb.append("smiley_path: \"").append(homePath).append("/smiley/\",");

		sb.append("enterMode: ").append(getLineMode((EEditorLineMode) cp.getBeanProperty("enterMode"))).append(",");
		sb.append("shiftEnterMode: ").append(getLineMode((EEditorLineMode) cp.getBeanProperty("shiftEnterMode"))).append(",");

		sb.append("language: \"").append(getLanguage()).append("\",");
		sb.append("autoUpdateElement: false,");
		String[] removePlugins = new String[] {};
		if (!(Boolean) cp.getBeanProperty("elementsPath")) {
			removePlugins = ArrayUtils.add(removePlugins, "elementspath");
		}
		if (removePlugins.length > 0) {
			sb.append("removePlugins: '").append(StringUtils.join(removePlugins, ",")).append("',");
		}
		sb.append("startupFocus: ").append(cp.getBeanProperty("startupFocus")).append(",");
		sb.append("toolbarCanCollapse: ").append(cp.getBeanProperty("toolbarCanCollapse")).append(",");
		sb.append("resize_enabled: ").append(cp.getBeanProperty("resizeEnabled")).append(",");
		final String height = (String) cp.getBeanProperty("height");
		if (StringUtils.hasText(height)) {
			sb.append("height: \"").append(height).append("\",");
		}

		sb.append("on: {");
		sb.append("  instanceReady: function(ev) { CKEDITOR._loading = false; ");
		final String jsLoadedCallback = (String) cp.getBeanProperty("jsLoadedCallback");
		if (StringUtils.hasText(jsLoadedCallback)) {
			sb.append(jsLoadedCallback);
		}
		sb.append("  }");
		sb.append("}");
		sb.append("});");
		final Toolbar toolbar = Toolbar.map.get(htmlEditor.getToolbar());
		int size;
		if (toolbar != null && (size = toolbar.size()) > 0) {
			sb.append("CKEDITOR.config.toolbar = [");
			for (int i = 0; i < size; i++) {
				if (i > 0) {
					sb.append(",");
				}
				final String[] sArr = toolbar.get(i);
				if (sArr.length == 0) {
					sb.append("'/'");
				} else {
					sb.append(JsonUtils.array2json(sArr));
				}
			}
			sb.append("];");
		}

		if (hasTextarea) {
			sb.append("$(\"").append(textarea).append("\").htmlEditor = ");
			sb.append(actionFunc).append(".editor;");
		}

		final String htmlContent = (String) cp.getBeanProperty("htmlContent");
		if (StringUtils.hasText(htmlContent)) {
			sb.append(actionFunc).append(".editor.setData(\"");
			sb.append(JavascriptUtils.escape(htmlContent)).append("\");");
		}

		final StringBuilder sb2 = new StringBuilder();
		sb2.append("var act = $Actions[\"").append(cp.getComponentName()).append("\"];");
		sb2.append("if (act && act.editor) { CKEDITOR.remove(act.editor); }");
		return ComponentRenderUtils.wrapActionFunction(cp, sb.toString(), sb2.toString());
	}

	private String getLanguage() {
		final Locale l = I18n.getLocale();
		if (l.equals(Locale.SIMPLIFIED_CHINESE)) {
			return "zh-cn";
		} else {
			return "en";
		}
	}

	private String getLineMode(final EEditorLineMode lineMode) {
		if (lineMode == EEditorLineMode.br) {
			return "CKEDITOR.ENTER_BR";
		} else if (lineMode == EEditorLineMode.div) {
			return "CKEDITOR.ENTER_DIV";
		} else {
			return "CKEDITOR.ENTER_P";
		}
	}
}
