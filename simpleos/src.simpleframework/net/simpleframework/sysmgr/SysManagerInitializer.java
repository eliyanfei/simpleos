package net.simpleframework.sysmgr;

import javax.servlet.ServletContext;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.sysmgr.dict.DefaultSysDictApplicationModule;
import net.simpleframework.sysmgr.dict.ISysDictApplicationModule;
import net.simpleframework.sysmgr.dict.component.dictselect.DictSelectRegistry;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class SysManagerInitializer extends AbstractInitializer {
	private ISysDictApplicationModule sysDictApplicationModule;

	@Override
	public void doInit(final IApplication application) {
		IInitializer.Utils.deploySqlScript(SysManagerInitializer.class, application, "sysmgr");
		super.doInit(application);

		if (application instanceof IWebApplication) {
			final ServletContext servletContext = ((IWebApplication) application).getServletContext();
			final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
			factory.regist(new DictSelectRegistry(servletContext));
		}
	}

	@Override
	protected Class<?>[] getI18n() {
		return new Class<?>[] { SysManagerInitializer.class };
	}

	public ISysDictApplicationModule getSysDictApplicationModule() {
		if (sysDictApplicationModule == null) {
			sysDictApplicationModule = new DefaultSysDictApplicationModule();
		}
		return sysDictApplicationModule;
	}

	public void setSysDictApplicationModule(final ISysDictApplicationModule sysDictApplicationModule) {
		this.sysDictApplicationModule = sysDictApplicationModule;
	}
}
