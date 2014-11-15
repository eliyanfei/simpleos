package net.simpleframework.workflow;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.workflow.impl.OrganizationAdapter;
import net.simpleframework.workflow.impl.WorkflowEngine;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class WorkflowUtils {
	public static WorkflowEngine applicationModule;

	public static String deployPath;

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static OrganizationAdapter org() {
		return applicationModule.getOrganizationAdapter();
	}

	public static IProcessModelManager pmm() {
		return applicationModule.getProcessModelMgr();
	}

	public static IProcessManager pm() {
		return applicationModule.getProcessMgr();
	}

	public static IActivityManager am() {
		return applicationModule.getActivityMgr();
	}

	public static IWorkitemManager wm() {
		return applicationModule.getWorkitemMgr();
	}
}
