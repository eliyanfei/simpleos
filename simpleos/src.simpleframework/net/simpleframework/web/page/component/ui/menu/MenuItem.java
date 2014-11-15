package net.simpleframework.web.page.component.ui.menu;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.core.AbstractElementBean;
import net.simpleframework.util.LocaleI18n;

import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MenuItem extends AbstractElementBean {
	private final MenuBean menuBean;

	private final MenuItem parent;

	private Collection<MenuItem> children;

	private String ref;

	private String title;

	private String url;

	private String icon;

	private boolean disabled;

	private String jsSelectCallback;

	private boolean checkbox, checked;

	private String jsCheckCallback;

	private String description;

	public MenuItem(final Element element, final MenuBean menuBean, final MenuItem parent) {
		super(element);
		this.menuBean = menuBean;
		this.parent = parent;
	}

	public MenuItem(final MenuBean menuBean, final MenuItem parent) {
		this(null, menuBean, parent);
	}

	public MenuItem(final MenuBean menuBean) {
		this(null, menuBean, null);
	}

	public Collection<MenuItem> getChildren() {
		if (children == null) {
			children = new ArrayList<MenuItem>();
		}
		return children;
	}

	public void parseElement() {
		super.parseElement();
		if (children != null)
			for (MenuItem menuItem : children) {
				menuItem.parseElement();
			}
	}

	public MenuBean getMenuBean() {
		return menuBean;
	}

	public MenuItem getParent() {
		return parent;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(final String ref) {
		this.ref = ref;
	}

	public String getTitle() {
		return LocaleI18n.replaceI18n(title);
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getUrl() {
		return url == null ? "" : url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(final boolean disabled) {
		this.disabled = disabled;
	}

	public String getJsSelectCallback() {
		return jsSelectCallback;
	}

	public void setJsSelectCallback(final String jsSelectCallback) {
		this.jsSelectCallback = jsSelectCallback;
	}

	public String getJsCheckCallback() {
		return jsCheckCallback;
	}

	public void setJsCheckCallback(final String jsCheckCallback) {
		this.jsCheckCallback = jsCheckCallback;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(final boolean checkbox) {
		this.checkbox = checkbox;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(final boolean checked) {
		this.checked = checked;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsSelectCallback", "jsCheckCallback", "description" };
	}
}
