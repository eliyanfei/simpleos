package net.itsite.complaint;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;

/**
 * @author lg
 *
 */
public class ComplaintInitializer extends AbstractInitializer {
	private IComplaintApplicationModule dataLibApplicationModule;

	@Override
	public void doInit(IApplication application) {
		try {
			super.doInit(application);
			dataLibApplicationModule = new ComplaintApplicationModule();
			dataLibApplicationModule.init(this);
			IInitializer.Utils.deploySqlScript(getClass(), application, ComplaintUtils.deployPath);
		} catch (Exception e) {
		}
	}
}
