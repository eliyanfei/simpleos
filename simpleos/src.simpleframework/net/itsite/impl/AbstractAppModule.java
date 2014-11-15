package net.itsite.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.itsite.i.IAppModule;
import net.itsite.permission.PlatformMenuItem;
import net.itsite.utils.Dom4jUtils;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

public abstract class AbstractAppModule implements IAppModule {

	protected Log logger = LogFactory.getLog(getClass());

	protected Lock lock = new ReentrantLock();

	public Collection<MenuItem> getMenuItems() {
		final List<MenuItem> menus = new ArrayList<MenuItem>();
		try {
			lock.lock();
			final String menusFileName = "/" + getClass().getPackage().getName().replace(".", "/") + "/menus.xml";
			final Document doc = Dom4jUtils.createDocument(getClass().getResourceAsStream(menusFileName));
			final Element root = doc.getRootElement();
			final List<Element> eleList = root.elements("menu");
			for (final Element ele : eleList) {
				final PlatformMenuItem menuItem = new PlatformMenuItem(null);
				setProperty(ele, menuItem);
				menus.add(menuItem);
				getMenuItemsByParent(ele, menuItem);
			}
		} catch (final Exception e) {
			logger.info("[" + getClass() + "] not found menu.xml.");
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return menus;
	}

	protected void setProperty(final Element element, final Object menuBean) throws Exception {
		final Iterator<?> it = element.attributeIterator();
		while (it.hasNext()) {
			final Attribute attribute = (Attribute) it.next();
			final String value = attribute.getValue();
			BeanUtils.setProperty(menuBean, attribute.getName(), value);
		}
	}

	@SuppressWarnings("unchecked")
	private void getMenuItemsByParent(final Element parentElement, final MenuItem parentMenu) throws Exception {
		if (parentElement == null || parentMenu == null) {
			return;
		}
		final List<Element> eleList = parentElement.elements("menu");
		for (final Element ele : eleList) {
			final PlatformMenuItem menuItem = new PlatformMenuItem(null);
			setProperty(ele, menuItem);
			parentMenu.getChildren().add(menuItem);
			getMenuItemsByParent(ele, menuItem);
		}
	}

}
