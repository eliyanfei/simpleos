package net.simpleframework.web.page.component.ui.syntaxhighlighter;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SyntaxHighlighterBean extends AbstractComponentBean {
	private ESyntaxHighlighterTheme shTheme;

	private String jsSelectedCallback; // editor

	public SyntaxHighlighterBean(final IComponentRegistry componentRegistry,
			final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public ESyntaxHighlighterTheme getShTheme() {
		return shTheme == null ? ESyntaxHighlighterTheme.shThemeDefault : shTheme;
	}

	public void setShTheme(final ESyntaxHighlighterTheme shTheme) {
		this.shTheme = shTheme;
	}

	public String getJsSelectedCallback() {
		return jsSelectedCallback;
	}

	public void setJsSelectedCallback(final String jsSelectedCallback) {
		this.jsSelectedCallback = jsSelectedCallback;
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultSyntaxHighlighterHandle.class;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsSelectedCallback" };
	}
}
