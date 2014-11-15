package net.simpleframework.web.page.component.ui.tabs;

import net.simpleframework.core.AbstractElementBean;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TabItem extends AbstractElementBean {
	private String title;

	private String content;

	private String contentStyle;

	private String contentRef;

	private boolean cache;

	private String jsActiveCallback;

	private String jsContentLoadedCallback;

	public TabItem(final Element element) {
		super(element);
	}

	public TabItem() {
		this(null);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public String getContentRef() {
		return contentRef;
	}

	public void setContentRef(final String contentRef) {
		this.contentRef = contentRef;
	}

	public String getContentStyle() {
		return contentStyle;
	}

	public void setContentStyle(final String contentStyle) {
		this.contentStyle = contentStyle;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(final boolean cache) {
		this.cache = cache;
	}

	public String getJsActiveCallback() {
		return jsActiveCallback;
	}

	public void setJsActiveCallback(final String jsActiveCallback) {
		this.jsActiveCallback = jsActiveCallback;
	}

	public String getJsContentLoadedCallback() {
		return jsContentLoadedCallback;
	}

	public void setJsContentLoadedCallback(final String jsContentLoadedCallback) {
		this.jsContentLoadedCallback = jsContentLoadedCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsActiveCallback", "jsContentLoadedCallback" };
	}
}
