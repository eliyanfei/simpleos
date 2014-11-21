package net.simpleframework.organization.impl;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.AbstractOrganizationManager;
import net.simpleframework.organization.EJobType;
import net.simpleframework.organization.IDepartment;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.jobrule.JrAccountLocked;
import net.simpleframework.organization.jobrule.JrAccountNormal;
import net.simpleframework.organization.jobrule.JrAccountRegister;
import net.simpleframework.organization.jobrule.JrAdmin;
import net.simpleframework.organization.jobrule.JrAnonymous;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobChartManager extends AbstractOrganizationManager<IJobChart> {

	@Override
	public Class<? extends IJobChart> getBeanClass() {
		return JobChart.class;
	}

	public IJobChart getJobChartByName(final String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return queryForObject(new ExpressionValue("name=?", new Object[] { name }));
	}

	public IJobChart createJobChart(final ID departmentId, final String name, final String text) {
		final JobChart jc = new JobChart();
		jc.setDepartmentId(departmentId);
		jc.setName(name);
		jc.setText(text);
		return jc;
	}

	public IDataObjectQuery<? extends IJobChart> jobCharts(final IDepartment department) {
		ExpressionValue ev;
		if (department == null) {
			ev = new ExpressionValue(Table.nullExpr(getTable(), "departmentid")
					+ " order by oorder desc");
		} else {
			ev = new ExpressionValue("departmentid=? order by oorder desc",
					new Object[] { department.getId() });
		}
		return query(ev);
	}

	static final String JOBCHART_ID = "__jobchart_Id";

	public String getJobChartIdParameterName() {
		return JOBCHART_ID;
	}

	public static void init() {
		// 加入系统视图
		IJobChart jc = OrgUtils.jcm().getJobChartByName(IJobChart.sysjc);
		if (jc == null) {
			jc = OrgUtils.jcm().createJobChart(null, IJobChart.sysjc,
					LocaleI18n.getMessage("JobChartManager." + IJobChart.sysjc));
			OrgUtils.jcm().insert(jc);
		}

		IJob job = OrgUtils.jm().getJobByName(IJob.sj_manager);
		if (job == null) {
			job = OrgUtils.jm().createJob(jc, IJob.sj_manager,
					LocaleI18n.getMessage("JobChartManager." + IJob.sj_manager));
			job.setJobType(EJobType.normal);
			OrgUtils.jm().insert(job);
		}
		final String[] jobNames = new String[] { IJob.sj_admin, IJob.sj_anonymous,
				IJob.sj_account_normal, IJob.sj_account_locked, IJob.sj_account_register };
		final Class<?>[] jrClass = new Class<?>[] { JrAdmin.class, JrAnonymous.class,
				JrAccountNormal.class, JrAccountLocked.class, JrAccountRegister.class };
		for (int i = jobNames.length - 1; i >= 0; i--) {
			final String jn = jobNames[i];
			job = OrgUtils.jm().getJobByName(jn);
			if (job == null) {
				job = OrgUtils.jm().createJob(jc, jn, LocaleI18n.getMessage("JobChartManager." + jn));
				job.setJobType(EJobType.handle);
				job.setRuleHandle(jrClass[i].getName());
				OrgUtils.jm().insert(job);
			}
		}
	}
}
