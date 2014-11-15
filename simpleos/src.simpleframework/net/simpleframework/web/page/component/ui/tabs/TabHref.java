package net.simpleframework.web.page.component.ui.tabs;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.util.LocaleI18n;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TabHref {
	private String text, href;

	private EMatchMethod matchMethod;

	private List<TabHref> children;

	private String html2;

	public TabHref(final String text, final String href) {
		this.text = LocaleI18n.replaceI18n(text);
		this.href = href;
	}

	public EMatchMethod getMatchMethod() {
		return matchMethod == null ? EMatchMethod.endsWith : matchMethod;
	}

	public void setMatchMethod(final EMatchMethod matchMethod) {
		this.matchMethod = matchMethod;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getHref() {
		return href;
	}

	public void setHref(final String href) {
		this.href = href;
	}

	public List<TabHref> getChildren() {
		if (children == null) {
			children = new ArrayList<TabHref>();
		}
		return children;
	}

	public String getHtml2() {
		return html2;
	}

	public void setHtml2(final String html2) {
		this.html2 = html2;
	}

	@Override
	public String toString() {
		return getText();
	}
}
