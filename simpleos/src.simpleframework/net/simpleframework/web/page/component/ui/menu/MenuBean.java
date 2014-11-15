package net.simpleframework.web.page.component.ui.menu;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.web.page.PageDocument;
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
public class MenuBean extends AbstractContainerBean {

	private Collection<MenuItem> menuItems;

	private EMenuEvent menuEvent;

	private String iconPath;

	private String jsBeforeShowCallback;

	private String minWidth;

	public MenuBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
		super(componentRegistry, pageDocument, element);
		setMinWidth("120");
	}

	public Collection<MenuItem> getMenuItems() {
		if (menuItems == null) {
			menuItems = new ArrayList<MenuItem>();
		}
		return menuItems;
	}

	public void parseElement() {
		super.parseElement();
		if (menuItems != null)
			for (MenuItem menuItem : menuItems) {
				menuItem.parseElement();
			}
	}

	public EMenuEvent getMenuEvent() {
		return menuEvent;
	}

	public void setMenuEvent(final EMenuEvent menuEvent) {
		this.menuEvent = menuEvent;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(final String iconPath) {
		this.iconPath = iconPath;
	}

	public String getMinWidth() {
		return minWidth;
	}

	public void setMinWidth(final String minWidth) {
		this.minWidth = minWidth;
	}

	public String getJsBeforeShowCallback() {
		return jsBeforeShowCallback;
	}

	public void setJsBeforeShowCallback(final String jsBeforeShowCallback) {
		this.jsBeforeShowCallback = jsBeforeShowCallback;
	}

	@Override
	public String getSelector() {
		return selector;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "jsBeforeShowCallback" };
	}
}
