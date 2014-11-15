package net.simpleframework.applets;

import javax.servlet.ServletContext;

import net.simpleframework.applets.attention.DefaultAttentionApplicationModule;
import net.simpleframework.applets.attention.IAttentionApplicationModule;
import net.simpleframework.applets.invite.DefaultInviteApplicationModule;
import net.simpleframework.applets.invite.IInviteApplicationModule;
import net.simpleframework.applets.notification.DefaultNotificationApplicationModule;
import net.simpleframework.applets.notification.INotificationApplicationModule;
import net.simpleframework.applets.notification.component.window.MessageWindowRegistry;
import net.simpleframework.applets.openid.DefaultOpenIDApplicationModule;
import net.simpleframework.applets.openid.IOpenIDApplicationModule;
import net.simpleframework.applets.tag.DefaultTagApplicationModule;
import net.simpleframework.applets.tag.ITagApplicationModule;
import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AppletsInitializer extends AbstractInitializer {
	private INotificationApplicationModule notificationApplicationModule;

	private IInviteApplicationModule inviteApplicationModule;

	private IOpenIDApplicationModule openIDApplicationModule;

	private IAttentionApplicationModule attentionApplicationModule;

	private ITagApplicationModule tagApplicationModule;

	@Override
	public void doInit(final IApplication application) {
		IInitializer.Utils.deploySqlScript(AppletsInitializer.class, application, "applets");
		super.doInit(application);

		regist(application);
	}

	@Override
	protected Class<?>[] getI18n() {
		return new Class<?>[] { AppletsInitializer.class };
	}

	private void regist(final IApplication application) {
		if (!(application instanceof IWebApplication)) {
			return;
		}
		final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
		final ServletContext servletContext = ((IWebApplication) application).getServletContext();
		factory.regist(new MessageWindowRegistry(servletContext));
	}

	public INotificationApplicationModule getNotificationApplicationModule() {
		if (notificationApplicationModule == null) {
			notificationApplicationModule = new DefaultNotificationApplicationModule();
		}
		return notificationApplicationModule;
	}

	public void setNotificationApplicationModule(
			final INotificationApplicationModule notificationApplicationModule) {
		this.notificationApplicationModule = notificationApplicationModule;
	}

	public IInviteApplicationModule getInviteApplicationModule() {
		if (inviteApplicationModule == null) {
			inviteApplicationModule = new DefaultInviteApplicationModule();
		}
		return inviteApplicationModule;
	}

	public void setInviteApplicationModule(final IInviteApplicationModule inviteApplicationModule) {
		this.inviteApplicationModule = inviteApplicationModule;
	}

	public IOpenIDApplicationModule getOpenIDApplicationModule() {
		if (openIDApplicationModule == null) {
			openIDApplicationModule = new DefaultOpenIDApplicationModule();
		}
		return openIDApplicationModule;
	}

	public void setOpenIDApplicationModule(final IOpenIDApplicationModule openIDApplicationModule) {
		this.openIDApplicationModule = openIDApplicationModule;
	}

	public IAttentionApplicationModule getAttentionApplicationModule() {
		if (attentionApplicationModule == null) {
			attentionApplicationModule = new DefaultAttentionApplicationModule();
		}
		return attentionApplicationModule;
	}

	public void setAttentionApplicationModule(
			final IAttentionApplicationModule attentionApplicationModule) {
		this.attentionApplicationModule = attentionApplicationModule;
	}

	public ITagApplicationModule getTagApplicationModule() {
		if (tagApplicationModule == null) {
			tagApplicationModule = new DefaultTagApplicationModule();
		}
		return tagApplicationModule;
	}

	public void setTagApplicationModule(final ITagApplicationModule tagApplicationModule) {
		this.tagApplicationModule = tagApplicationModule;
	}
}
