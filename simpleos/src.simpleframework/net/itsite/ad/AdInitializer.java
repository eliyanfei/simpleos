package net.itsite.ad;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;

/**
 *
 */
public class AdInitializer extends AbstractInitializer {
	private IAdApplicationModule adApplicationModule;


	@Override
	public void doInit(IApplication application) {
		try {
			super.doInit(application);
			adApplicationModule = new AdApplicationModule();
			adApplicationModule.init(this);
			IInitializer.Utils.deploySqlScript(getClass(), application, AdUtils.deployPath);
			
		} catch (Exception e) {
		}
	}
}
