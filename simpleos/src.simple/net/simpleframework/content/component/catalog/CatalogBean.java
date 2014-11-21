package net.simpleframework.content.component.catalog;

import net.simpleframework.content.AbstractContentBean;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageDocument;
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
public class CatalogBean extends AbstractContentBean {

	private boolean cookies = true;

	private boolean draggable = true, dynamicTree;

	private boolean showContextMenu = true;

	private String rootText = LocaleI18n.getMessage("CatalogBean.0");

	private String jobOwner;

	private String jsLoadedCallback;

	public CatalogBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
	}

	public boolean isCookies() {
		return cookies;
	}

	public void setCookies(final boolean cookies) {
		this.cookies = cookies;
	}

	@Override
	protected Class<? extends IComponentHandle> getDefaultHandleClass() {
		return DefaultCatalogHandle.class;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(final boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isShowContextMenu() {
		return showContextMenu;
	}

	public void setShowContextMenu(final boolean showContextMenu) {
		this.showContextMenu = showContextMenu;
	}

	public boolean isDynamicTree() {
		return dynamicTree;
	}

	public void setDynamicTree(final boolean dynamicTree) {
		this.dynamicTree = dynamicTree;
	}

	public String getRootText() {
		return rootText;
	}

	public void setRootText(final String rootText) {
		this.rootText = rootText;
	}

	public String getJobOwner() {
		return jobOwner;
	}

	public void setJobOwner(final String jobOwner) {
		this.jobOwner = jobOwner;
	}

	public String getJsLoadedCallback() {
		return jsLoadedCallback;
	}

	public void setJsLoadedCallback(final String jsLoadedCallback) {
		this.jsLoadedCallback = jsLoadedCallback;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsLoadedCallback" };
	}
}
