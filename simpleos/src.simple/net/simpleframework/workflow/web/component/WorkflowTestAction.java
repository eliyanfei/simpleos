package net.simpleframework.workflow.web.component;

import net.simpleframework.organization.IUser;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;
import net.simpleframework.workflow.ProcessModelBean;
import net.simpleframework.workflow.WorkflowUtils;

public class WorkflowTestAction extends AbstractAjaxRequestHandle {
	@Override
	public IForward ajaxProcess(final ComponentParameter compParameter) {
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account == null) {
			return null;
		}
		// final IUser workflowUser = new WorkflowUser(account.getUser());
		// testAddProcessModel(workflowUser);
		// testModels(workflowUser);
		return null;
	}

	void testModels(final IUser workflowUser) {
		// final IDataObjectQuery<ProcessModelBean> query =
		// WorkflowUtils.applicationModule
		// .getProcessModelMgr().models(workflowUser);
		// ProcessModelBean bean;
		// while ((bean = query.next()) != null) {
		// System.out.println(bean);
		// }
	}

	void testAddProcessModel(final IUser workflowUser) {
		final ProcessModelBean processModel = WorkflowUtils.pmm().add(workflowUser, null);
		System.out.println(processModel);
	}
}
