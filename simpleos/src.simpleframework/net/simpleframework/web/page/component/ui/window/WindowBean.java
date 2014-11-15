package net.simpleframework.web.page.component.ui.window;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WindowBean extends AbstractComponentBean {
	private String contentRef;

	private int top, left, width = 400, height = 300, minWidth = 200, minHeight = 100, maxWidth, maxHeight;

	private boolean destroyOnClose = true;

	private boolean resizable = true, draggable = true, minimize, maximize, modal = true;

	private boolean showCenter = false;

	private boolean popup = false;

	private String url;

	private String title;

	private String content;

	private boolean singleWindow = true;

	private String jsShownCallback, jsHiddenCallback;

	public WindowBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setRunImmediately(false);
	}

	public WindowBean(final PageDocument pageDocument, final Element element) {
		this(AbstractComponentRegistry.getRegistry(WindowRegistry.window), pageDocument, element);
	}

	@Override
	protected void beforeParse() {
		super.beforeParse();
	}

	@Override
	protected void endParse() {
		if (!isPopup()) {
			this.showCenter = true;
		}
	}

	public String getContentRef() {
		return contentRef;
	}

	public void setContentRef(final String contentRef) {
		this.contentRef = contentRef;
	}

	public boolean isPopup() {
		return popup;
	}

	public void setPopup(final boolean popup) {
		this.popup = popup;
	}

	public int getTop() {
		return top;
	}

	public void setTop(final int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(final int left) {
		this.left = left;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public int getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
	}

	public int getMinHeight() {
		return minHeight;
	}

	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(final int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(final int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(final boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(final boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isDestroyOnClose() {
		return destroyOnClose;
	}

	public void setDestroyOnClose(final boolean destroyOnClose) {
		this.destroyOnClose = destroyOnClose;
	}

	public boolean isMinimize() {
		return minimize;
	}

	public void setMinimize(final boolean minimize) {
		this.minimize = minimize;
	}

	public boolean isMaximize() {
		return maximize;
	}

	public void setMaximize(final boolean maximize) {
		this.maximize = maximize;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(final boolean modal) {
		this.modal = modal;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
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

	public boolean isShowCenter() {
		return showCenter;
	}

	public void setShowCenter(final boolean showCenter) {
		this.showCenter = showCenter;
	}

	public boolean isSingleWindow() {
		return singleWindow;
	}

	public void setSingleWindow(final boolean singleWindow) {
		this.singleWindow = singleWindow;
	}

	public String getJsShownCallback() {
		return jsShownCallback;
	}

	public void setJsShownCallback(final String jsShownCallback) {
		this.jsShownCallback = jsShownCallback;
	}

	public String getJsHiddenCallback() {
		return jsHiddenCallback;
	}

	public void setJsHiddenCallback(final String jsHiddenCallback) {
		this.jsHiddenCallback = jsHiddenCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsShownCallback", "jsHiddenCallback", "content" };
	}
}
