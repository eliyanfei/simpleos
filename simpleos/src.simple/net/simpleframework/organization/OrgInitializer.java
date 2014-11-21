package net.simpleframework.organization;

import javax.servlet.ServletContext;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.component.deptselect.DeptSelectRegistry;
import net.simpleframework.organization.component.jobchartselect.JobChartSelectRegistry;
import net.simpleframework.organization.component.jobselect.JobSelectRegistry;
import net.simpleframework.organization.component.login.LoginRegistry;
import net.simpleframework.organization.component.register.UserRegisterRegistry;
import net.simpleframework.organization.component.userpager.UserPagerRegistry;
import net.simpleframework.organization.component.userselect.UserSelectRegistry;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrgInitializer extends AbstractInitializer {

	public static final String deployName = "organization";

	private IOrganizationApplicationModule organizationApplicationModule;

	@Override
	public void doInit(final IApplication application) {
		IInitializer.Utils.deploySqlScript(OrgInitializer.class, application, deployName);
		super.doInit(application);
		if (application instanceof IWebApplication) {
			regist((IWebApplication) application);
		}
	}

	private void regist(final IWebApplication application) {
		final ServletContext servletContext = application.getServletContext();
		AccountSession.init(servletContext);

		final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
		factory.regist(new LoginRegistry(servletContext));
		factory.regist(new UserRegisterRegistry(servletContext));
		factory.regist(new UserPagerRegistry(servletContext));
		factory.regist(new UserSelectRegistry(servletContext));
		factory.regist(new DeptSelectRegistry(servletContext));
		factory.regist(new JobChartSelectRegistry(servletContext));
		factory.regist(new JobSelectRegistry(servletContext));

	}

	public IOrganizationApplicationModule getOrganizationApplicationModule() {
		if (organizationApplicationModule == null) {
			organizationApplicationModule = new OrganizationApplicationModule();
		}
		return organizationApplicationModule;
	}

	public void setOrganizationApplicationModule(
			final IOrganizationApplicationModule organizationApplicationModule) {
		this.organizationApplicationModule = organizationApplicationModule;
	}
}
