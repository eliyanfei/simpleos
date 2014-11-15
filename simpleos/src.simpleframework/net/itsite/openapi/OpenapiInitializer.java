package net.itsite.openapi;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;

/**
 * @author lg
 *
 */
public class OpenapiInitializer extends AbstractInitializer {
	private IOpenapiApplicationModule applicationModule;

	@Override
	public void doInit(IApplication application) {
		try {
			super.doInit(application);
			applicationModule = new OpenapiApplicationModule();
			applicationModule.init(this);
			IInitializer.Utils.deploySqlScript(getClass(), application, OpenapiUtils.deployPath);
		} catch (Exception e) {
		}
	}

}
