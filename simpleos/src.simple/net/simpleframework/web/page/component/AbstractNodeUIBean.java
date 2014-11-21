package net.simpleframework.web.page.component;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractNodeUIBean extends AbstractElementBean {
	private String id;

	private String text;

	// event
	private String jsClickCallback, jsDblclickCallback;

	public AbstractNodeUIBean(final Element element) {
		super(element);
	}

	public String getId() {
		if (!StringUtils.hasText(id)) {
			id = StringUtils.hash(getText());
		}
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getText() {
		return LocaleI18n.replaceI18n(text);
	}

	public void setText(final String text) {
		this.text = text;
	}

	public String getJsClickCallback() {
		return jsClickCallback;
	}

	public void setJsClickCallback(final String jsClickCallback) {
		this.jsClickCallback = jsClickCallback;
	}

	public String getJsDblclickCallback() {
		return jsDblclickCallback;
	}

	public void setJsDblclickCallback(final String jsDblclickCallback) {
		this.jsDblclickCallback = jsDblclickCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsClickCallback", "jsDblclickCallback" };
	}
}
