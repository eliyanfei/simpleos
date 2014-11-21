package net.simpleframework.web.page.component.ui.listbox;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.AbstractComponentRegistry;
import net.simpleframework.web.page.component.AbstractContainerBean;
import net.simpleframework.web.page.component.IComponentRegistry;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ListboxBean extends AbstractContainerBean {

	private Collection<ListItem> listItems;

	private boolean checkbox;

	private boolean tooltip;

	private String jsClickCallback, jsDblclickCallback;

	public ListboxBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument,
			final Element element) {
		super(componentRegistry, pageDocument, element);
		setWidth("200");
		setHeight("180");
	}

	public ListboxBean(final PageDocument pageDocument, final Element element) {
		this(AbstractComponentRegistry.getRegistry(ListboxRegistry.listbox), pageDocument, element);
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(final boolean checkbox) {
		this.checkbox = checkbox;
	}

	public boolean isTooltip() {
		return tooltip;
	}

	public void setTooltip(final boolean tooltip) {
		this.tooltip = tooltip;
	}

	public Collection<ListItem> getListItems() {
		if (listItems == null) {
			listItems = new ArrayList<ListItem>();
		}
		return listItems;
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
