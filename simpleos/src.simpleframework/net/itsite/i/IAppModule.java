package net.itsite.i;

import java.util.Collection;

import net.simpleframework.web.page.component.ui.menu.MenuItem;

public interface IAppModule {

	String getModuleName();

	String getModuleText();

	Collection<MenuItem> getMenuItems();
}
