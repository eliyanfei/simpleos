package net.simpleos;

import java.beans.Beans;
import java.util.Collection;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;
import net.simpleframework.web.page.component.ui.list.ListRegistry;
import net.simpleos.impl.ASimpleosAppclicationModule;
import net.simpleos.utils.ReflectUtils;

/**
 * 
 * @author 李岩飞 
 * @date 2014年11月19日 下午4:58:22 
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 */
public class SimpleosInitializer extends AbstractInitializer {

	@Override
	public void doInit(IApplication application) {
		super.doInit(application);
		try {
			IInitializer.Utils.deploySqlScript(getClass(), application, "simpleos");
			IInitializer.Utils.deploySqlScript(getClass(), application, "simpleos");
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
			ReflectUtils.createSharedReflections("classes", "bin", "app.", "simple.");
			try {
				final Collection<String> subTypes = ReflectUtils.listSubClass(ASimpleosAppclicationModule.class);//
				for (final String subType : subTypes) {
					final ASimpleosAppclicationModule impl = ReflectUtils.initClass(subType, ASimpleosAppclicationModule.class);
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
	}

	private void registryComponet(final IWebApplication webApplication) {
		ComponentRegistryFactory.getInstance().regist(new ListRegistry(webApplication.getServletContext()));
	}

	@Override
	protected Class<?>[] getI18n() {
		return new Class<?>[] { SimpleosUtil.class };
	}

}
