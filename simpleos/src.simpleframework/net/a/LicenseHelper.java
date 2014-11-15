package net.a;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import net.itsite.HomePageAppModule;
import net.itsite.i.IAppModule;
import net.itsite.permission.PlatformMenuItem;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class LicenseHelper {
	static final Log logger = LogFactory.getLog(LicenseHelper.class);
	public static final List<MenuItem> menuList = new ArrayList<MenuItem>();

	public static void registerAppModule(IAppModule appModule) {
		menuList.addAll(appModule.getMenuItems());
		Collections.sort(menuList, new Comparator<MenuItem>() {

			@Override
			public int compare(final MenuItem o1, final MenuItem o2) {
				final PlatformMenuItem menu1 = (PlatformMenuItem) o1;
				final PlatformMenuItem menu2 = (PlatformMenuItem) o2;
				if (menu1.getIndex() > menu2.getIndex()) {
					return 1;
				} else if (menu1.getIndex() < menu2.getIndex()) {
					return -1;
				}
				return 0;
			}
		});
	}

	public static void main(final String[] args) {
		try {
			ItSiteUtil.registerAppModule(new HomePageAppModule());
			final Properties p = new Properties();
			genLicense(new File("C:\\Users\\lg\\.simple\\prjmgr\\lic.key"), "", 365 * 10, p);
			// System.out.println(genRegisteredCode(IoHelper.getMacAddress()));
		} catch (final Exception ex) {
		}
	}

	private static void genLicense(File file, String string, int i, Properties p) {

	}
}
