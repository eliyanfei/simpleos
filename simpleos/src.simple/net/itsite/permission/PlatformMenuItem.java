package net.itsite.permission;

import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.dom4j.Element;

public class PlatformMenuItem extends MenuItem {
	String v;//版本信息
	int index;

	public PlatformMenuItem(final Element element, final MenuBean menuBean, final PlatformMenuItem parent) {
		super(element, menuBean, parent);
	}

	public PlatformMenuItem(final MenuBean menuBean, final PlatformMenuItem parent) {
		super(menuBean, parent);
	}

	public PlatformMenuItem(final MenuBean menuBean) {
		super(menuBean);
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getV() {
		return v;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return getTitle() == null ? "" : getTitle();
	}

}
