package net.simpleframework.workflow;

import javax.servlet.ServletContext;

import net.simpleframework.core.AbstractInitializer;
import net.simpleframework.core.IApplication;
import net.simpleframework.core.IInitializer;
import net.simpleframework.web.IWebApplication;
import net.simpleframework.web.page.component.ComponentRegistryFactory;
import net.simpleframework.workflow.impl.WorkflowEngine;
import net.simpleframework.workflow.web.component.modellist.ModelListRegistry;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkflowInitializer extends AbstractInitializer {

	private IWorkflowEngine workflowEngine;

	@Override
	public void doInit(final IApplication application) {
		IInitializer.Utils.deploySqlScript(WorkflowInitializer.class, application, "workflow");
		super.doInit(application);
		regist(application);
	}

	private void regist(final IApplication application) {
		if (!(application instanceof IWebApplication)) {
			return;
		}
		final ServletContext servletContext = ((IWebApplication) application).getServletContext();
		final ComponentRegistryFactory factory = ComponentRegistryFactory.getInstance();
		factory.regist(new ModelListRegistry(servletContext));
	}

	public IWorkflowEngine getWorkflowEngine() {
		if (workflowEngine == null) {
			workflowEngine = new WorkflowEngine();
		}
		return workflowEngine;
	}

	public void setWorkflowEngine(final IWorkflowEngine workflowEngine) {
		this.workflowEngine = workflowEngine;
	}

	@Override
	protected Class<?>[] getI18n() {
		return new Class[] { WorkflowInitializer.class };
	}
}
