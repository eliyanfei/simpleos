package net.simpleframework.workflow.impl;

import java.util.Map;

import net.simpleframework.core.IInitializer;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.web.AbstractWebApplicationModule;
import net.simpleframework.workflow.ActivityBean;
import net.simpleframework.workflow.ActivityLobBean;
import net.simpleframework.workflow.DelegationBean;
import net.simpleframework.workflow.IActivityManager;
import net.simpleframework.workflow.IProcessManager;
import net.simpleframework.workflow.IProcessModelManager;
import net.simpleframework.workflow.IWorkflowEngine;
import net.simpleframework.workflow.IWorkitemManager;
import net.simpleframework.workflow.ProcessBean;
import net.simpleframework.workflow.ProcessLobBean;
import net.simpleframework.workflow.ProcessModelBean;
import net.simpleframework.workflow.ProcessModelLobBean;
import net.simpleframework.workflow.WorkflowUtils;
import net.simpleframework.workflow.WorkitemBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class WorkflowEngine extends AbstractWebApplicationModule implements IWorkflowEngine {

	@Override
	public void init(final IInitializer initializer) {
		super.init(initializer);
		doInit(WorkflowUtils.class, "workflow");
	}

	public static Table simple_wf_model = new Table("simple_wf_model");
	public static Table simple_wf_model_lob = new Table("simple_wf_model_lob");
	public static Table simple_wf_process = new Table("simple_wf_process");
	public static Table simple_wf_process_lob = new Table("simple_wf_process_lob");
	public static Table simple_wf_delegation = new Table("simple_wf_delegation");
	public static Table simple_wf_activity = new Table("simple_wf_activity");
	public static Table simple_wf_activity_lob = new Table("simple_wf_activity_lob");
	public static Table simple_wf_workitem = new Table("simple_wf_workitem");
	static {
		simple_wf_model_lob.setNoCache(true);
		simple_wf_process_lob.setNoCache(true);
		simple_wf_activity_lob.setNoCache(true);
	}

	@Override
	protected void putTables(final Map<Class<?>, Table> tables) {
		tables.put(ProcessModelBean.class, simple_wf_model);
		tables.put(ProcessModelLobBean.class, simple_wf_model_lob);
		tables.put(ProcessBean.class, simple_wf_process);
		tables.put(ProcessLobBean.class, simple_wf_process_lob);
		tables.put(DelegationBean.class, simple_wf_delegation);
		tables.put(ActivityBean.class, simple_wf_activity);
		tables.put(ActivityLobBean.class, simple_wf_activity_lob);
		tables.put(WorkitemBean.class, simple_wf_workitem);
	}

	private IProcessModelManager processModelMgr;

	@Override
	public IProcessModelManager getProcessModelMgr() {
		if (processModelMgr == null) {
			processModelMgr = new ProcessModelManager();
		}
		return processModelMgr;
	}

	public void setProcessModelMgr(final IProcessModelManager processModelMgr) {
		this.processModelMgr = processModelMgr;
	}

	private IProcessManager processMgr;

	@Override
	public IProcessManager getProcessMgr() {
		if (processMgr == null) {
			processMgr = new ProcessManager();
		}
		return processMgr;
	}

	public void setProcessMgr(final IProcessManager processMgr) {
		this.processMgr = processMgr;
	}

	private IActivityManager activityMgr;

	public IActivityManager getActivityMgr() {
		if (activityMgr == null) {
			activityMgr = new ActivityManager();
		}
		return activityMgr;
	}

	public void setActivityMgr(final IActivityManager activityMgr) {
		this.activityMgr = activityMgr;
	}

	private IWorkitemManager workitemMgr;

	public IWorkitemManager getWorkitemMgr() {
		if (workitemMgr == null) {
			workitemMgr = new WorkitemManager();
		}
		return workitemMgr;
	}

	public void setWorkitemMgr(final IWorkitemManager workitemMgr) {
		this.workitemMgr = workitemMgr;
	}

	private OrganizationAdapter organizationAdapter;

	public OrganizationAdapter getOrganizationAdapter() {
		if (organizationAdapter == null) {
			organizationAdapter = new OrganizationAdapter();
		}
		return organizationAdapter;
	}

	public void setOrganizationAdapter(final OrganizationAdapter organizationAdapter) {
		this.organizationAdapter = organizationAdapter;
	}
}
