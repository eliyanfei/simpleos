package net.itsite;

import java.beans.Beans;
import java.util.Collection;

import net.a.ItSiteUtil;
import net.itsite.impl.AItSiteAppclicationModule;
import net.itsite.permission.PlatformUtis;
import net.itsite.utils.ReflectUtils;
import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;
import net.simpleframework.web.page.component.ui.list.ListRegistry;

public class ItSiteInitializer extends AbstractInitializer {

	@Override
	public void doInit(IApplication application) {
		super.doInit(application);
		try {
			IInitializer.Utils.deploySqlScript(getClass(), application, "prj");
			PlatformUtis.setDataSource(application.getDataSource());
			IInitializer.Utils.deploySqlScript(getClass(), application, "itsite");
			doInitInitializer(application);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doInitInitializer(IApplication application) {
		Beans.setDesignTime(true);
		registryComponet((IWebApplication) application);
		try {
			Beans.setDesignTime(true);
			ReflectUtils.createSharedReflections("classes", "bin", "app.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(AItSiteAppclicationModule.class);//
				for (final String subType : subTypes) {
					final AItSiteAppclicationModule impl = ReflectUtils.initClass(subType, AItSiteAppclicationModule.class);
					if (null == impl)
						continue;
					try {
						impl.init(this);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		LocaleI18n.addBasename(this.getClass());
		try {
			ItSiteUtil.registerAppModule(new HomePageAppModule());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void registryComponet(final IWebApplication webApplication) {
		ComponentRegistryFactory.getInstance().regist(new ListRegistry(webApplication.getServletContext()));
	}

	@Override
	protected Class<?>[] getI18n() {
		return new Class<?>[] { ItSiteUtil.class };
	}

}
