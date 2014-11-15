package net.simpleframework.my.message;

import java.util.Map;

import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultMessageApplicationModule extends AbstractWebApplicationModule implements IMessageApplicationModule {
	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(SimpleMessage.class, new Table("simple_my_message"));
	}

	@Override
	public Class<? extends ENotification> getNotificationClass() {
		return ENotification.class;
	}

	static final String deployName = "my/message";

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);

		MessageUtils.applicationModule = this;

		final IWebApplication application = getApplication();
		//		IInitializer.Utils.deployResource(MessageUtils.class, application, deployName);
		MessageUtils.deployPath = IInitializer.Utils.getApplicationDeployPath(application, deployName);
	}

	@Override
	public String getApplicationText() {
		return StringUtils.text(super.getApplicationText(), LocaleI18n.getMessage("DefaultMessageApplicationModule.0"));
	}

	private String systemMessageUrl;

	@Override
	public String getSystemMessageUrl(final PageRequestResponse requestResponse) {
		return systemMessageUrl;
	}

	public void setSystemMessageUrl(final String systemMessageUrl) {
		this.systemMessageUrl = systemMessageUrl;
	}
}
