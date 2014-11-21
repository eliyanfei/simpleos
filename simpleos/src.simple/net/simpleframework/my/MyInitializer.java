package net.simpleframework.my;

import javax.servlet.ServletContext;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.my.file.DefaultFileApplicationModule;
import net.simpleframework.my.file.IFileApplicationModule;
import net.simpleframework.my.file.component.fileselect.FileSelectRegistry;
import net.simpleframework.my.friends.DefaultFriendsApplicationModule;
import net.simpleframework.my.friends.IFriendsApplicationModule;
import net.simpleframework.my.home.DefaultHomeApplicationModule;
import net.simpleframework.my.home.IHomeApplicationModule;
import net.simpleframework.my.message.DefaultMessageApplicationModule;
import net.simpleframework.my.message.IMessageApplicationModule;
import net.simpleframework.my.space.DefaultSpaceApplicationModule;
import net.simpleframework.my.space.ISpaceApplicationModule;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class MyInitializer extends AbstractInitializer {

	private IFileApplicationModule fileApplicationModule;

	private IMessageApplicationModule messageApplicationModule;

	private IFriendsApplicationModule friendsApplicationModule;

	private IHomeApplicationModule homeApplicationModule;

	private ISpaceApplicationModule spaceApplicationModule;

	@Override
	public void doInit(final IApplication application) {
		IInitializer.Utils.deploySqlScript(MyInitializer.class, application, "my");
		super.doInit(application);
		if (application instanceof IWebApplication) {
			final ServletContext servletContext = ((IWebApplication) application).getServletContext();
			final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
			factory.regist(new FileSelectRegistry(servletContext));
		}
	}

	@Override
	protected Class<?>[] getI18n() {
		return new Class<?>[] { MyInitializer.class };
	}

	public IFileApplicationModule getFileApplicationModule() {
		if (fileApplicationModule == null) {
			fileApplicationModule = new DefaultFileApplicationModule();
		}
		return fileApplicationModule;
	}

	public void setFileApplicationModule(final IFileApplicationModule fileApplicationModule) {
		this.fileApplicationModule = fileApplicationModule;
	}

	public IMessageApplicationModule getMessageApplicationModule() {
		if (messageApplicationModule == null) {
			messageApplicationModule = new DefaultMessageApplicationModule();
		}
		return messageApplicationModule;
	}

	public void setMessageApplicationModule(final IMessageApplicationModule messageApplicationModule) {
		this.messageApplicationModule = messageApplicationModule;
	}

	public IHomeApplicationModule getHomeApplicationModule() {
		if (homeApplicationModule == null) {
			homeApplicationModule = new DefaultHomeApplicationModule();
		}
		return homeApplicationModule;
	}

	public void setFriendsApplicationModule(final IFriendsApplicationModule friendsApplicationModule) {
		this.friendsApplicationModule = friendsApplicationModule;
	}

	public void setHomeApplicationModule(final IHomeApplicationModule homeApplicationModule) {
		this.homeApplicationModule = homeApplicationModule;
	}

	public IFriendsApplicationModule getFriendsApplicationModule() {
		if (friendsApplicationModule == null) {
			friendsApplicationModule = new DefaultFriendsApplicationModule();
		}
		return friendsApplicationModule;
	}

	public ISpaceApplicationModule getSpaceApplicationModule() {
		if (spaceApplicationModule == null) {
			spaceApplicationModule = new DefaultSpaceApplicationModule();
		}
		return spaceApplicationModule;
	}

	public void setSpaceApplicationModule(final ISpaceApplicationModule spaceApplicationModule) {
		this.spaceApplicationModule = spaceApplicationModule;
	}
}
