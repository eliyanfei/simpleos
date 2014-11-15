package net.simpleframework.core;

import java.lang.reflect.Field;

import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractApplicationModule extends ALoggerAware implements IApplicationModule {

	@Override
	public Class<?> getEntityBeanClass() {
		return null;
	}

	private String applicationText;

	@Override
	public String getApplicationText() {
		return applicationText;
	}

	public void setApplicationText(final String applicationText) {
		this.applicationText = applicationText;
	}

	private String applicationTitle;

	@Override
	public String getApplicationTitle() {
		if (StringUtils.hasText(applicationTitle)) {
			return applicationTitle;
		} else {
			String title = getApplicationText();
			final String str = getApplication().getApplicationConfig().getTitle();
			if (StringUtils.hasText(str)) {
				title += " - " + str;
			}
			return StringUtils.blank(title);
		}
	}

	public void setApplicationTitle(final String applicationTitle) {
		this.applicationTitle = applicationTitle;
	}

	private IInitializer initializer;

	@Override
	public void init(final IInitializer initializer) {
		this.initializer = initializer;
	}

	@Override
	public IApplication getApplication() {
		return initializer.getApplication();
	}

	protected void doInit(final Class<?> utilClazz, final String deployName) {
		Field applicationModule = null;
		Field deployPath = null;
		try {
			applicationModule = utilClazz.getField("applicationModule");
			deployPath = utilClazz.getField("deployPath");
		} catch (final Exception ex) {
		}
		try {
			if (applicationModule != null) {
				applicationModule.set(null, this);
			}
			final IApplication application = getApplication();
			if (deployPath != null) {
				deployPath.set(null, IInitializer.Utils.getApplicationDeployPath(application, deployName));
			}
		} catch (final Exception e) {
			throw ApplicationModuleException.wrapException(e);
		}
	}
}