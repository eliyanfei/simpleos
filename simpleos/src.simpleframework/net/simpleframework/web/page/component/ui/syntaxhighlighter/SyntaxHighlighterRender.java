package net.simpleframework.web.page.component.ui.syntaxhighlighter;

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
public class SyntaxHighlighterRender extends AbstractComponentJavascriptRender {

	public SyntaxHighlighterRender(final IComponentRegistry componentRegistry) {
		super(componentRegistry);
	}

	@Override
	public String getJavascriptCode(final ComponentParameter compParameter) {
		final SyntaxHighlighterBean syntaxHighlighter = (SyntaxHighlighterBean) compParameter.componentBean;
		final String actionFunc = syntaxHighlighter.getActionFunction();
		final StringBuilder sb = new StringBuilder();
		final String hp = getResourceHomePath(compParameter) + "/js/";
		sb.append("SyntaxHighlighter.autoloader(");
		sb.append("['cpp', 'c++', 'c', '").append(hp).append("shBrushCpp.js'],");
		sb.append("['c#', 'c-sharp', 'csharp', '").append(hp).append("shBrushCSharp.js'],");
		sb.append("['css', '").append(hp).append("shBrushCss.js'],");
		sb.append("['groovy', '").append(hp).append("shBrushGroovy.js'],");
		sb.append("['java', '").append(hp).append("shBrushJava.js'],");
		sb.append("['js', 'javascript', '").append(hp).append("shBrushJScript.js'],");
		sb.append("['php', '").append(hp).append("shBrushPhp.js'],");
		sb.append("['py', 'python', '").append(hp).append("shBrushPython.js'],");
		sb.append("['ruby', 'rails', 'ror', 'rb', '").append(hp).append("shBrushRuby.js'],");
		sb.append("['sql', '").append(hp).append("shBrushSql.js'],");
		sb.append("['xml', 'xhtml', 'xslt', 'html', '").append(hp).append("shBrushXml.js']");
		sb.append(");");
		sb.append("SyntaxHighlighter.all({");
		sb.append("'toolbar' : false,");
		sb.append("'tab-size' : 2");
		sb.append("});");
		// loadScript
		final StringBuilder sb2 = new StringBuilder();
		sb.append(actionFunc).append(".editor = function() { $Actions['window_")
				.append(syntaxHighlighter.hashId()).append("'](); };");
		return ComponentRenderUtils.wrapActionFunction(compParameter, sb.toString(), sb2.toString());
	}
}
