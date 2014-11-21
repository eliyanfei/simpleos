package net.simpleframework.organization.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.organization.AbstractOrganizationManager;
import net.simpleframework.organization.EJobType;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobChart;
import net.simpleframework.organization.IJobMember;
import net.simpleframework.organization.IJobRule;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobManager extends AbstractOrganizationManager<IJob> {
	public JobManager() {
		addListener(new TableEntityAdapter() {
			@Override
			public void afterDelete(final ITableEntityManager manager,
					final IDataObjectValue dataObjectValue) {
				final Object[] values = dataObjectValue.getValues();
				final ArrayList<Object> al = new ArrayList<Object>(Arrays.asList(values));
				al.add(0, EMemberType.job);
				final ExpressionValue ev2 = new ExpressionValue("membertype=? and "
						+ DbUtils.getIdsSQLParam("memberid", values.length), al.toArray());
				OrgUtils.jmm().delete(ev2);
			}
		});
	}

	@Override
	public Class<? extends IJob> getBeanClass() {
		return Job.class;
	}

	public IJob getJobByName(final String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return queryForObject(new ExpressionValue("name=?", new Object[] { name }));
	}

	public int getCount(final IJobChart jc) {
		return getEntityManager().getCount(
				new ExpressionValue("jobchartid=?", new Object[] { jc.getId() }));
	}

	public Collection<IJob> children(final IJobChart jobChart) {
		final ExpressionValue ev = new ExpressionValue("jobchartid=? and "
				+ Table.nullExpr(getTable(), "parentid"), new Object[] { jobChart.getId() });
		return children(ev);
	}

	public Collection<IJob> children(final IJob parent) {
		return children(new ExpressionValue("parentid=?", new Object[] { parent.getId() }));
	}

	private Collection<IJob> children(final ExpressionValue ev) {
		final ArrayList<IJob> coll = new ArrayList<IJob>();
		final IDataObjectQuery<?> qs = query(ev);
		IJob job;
		while ((job = (IJob) qs.next()) != null) {
			coll.add(job);
		}
		return coll;
	}

	public IJob createJob(final IJobChart jobChart, final String name, final String text) {
		final Job job = new Job();
		job.setJobChartId(jobChart.getId());
		job.setName(name);
		job.setText(text);
		return job;
	}

	public boolean member(final IUser user, final IJob job, final Object... objects) {
		if (user == null) {
			return false;
		}
		final EJobType jt = job.getJobType();
		if (jt == EJobType.normal) {
			for (final IJobMember jobMember : OrgUtils.jmm().members(job)) {
				final Object id = jobMember.getMemberId();
				if (jobMember.getMemberType() == EMemberType.user) {
					if (user.getId().equals2(id)) {
						return true;
					}
				} else {
					return member(user, OrgUtils.jm().queryForObjectById(id), objects);
				}
			}
		} else if (jt == EJobType.handle) {
			final IJobRule jr = job.jobRuleHandle();
			return jr != null && jr.isMember(user, objects);
		} else if (jt == EJobType.script) {
		}
		return false;
	}

	public Collection<? extends IUser> users(final IJob job, final Object... objects) {
		final EJobType jobType = job.getJobType();
		if (jobType == EJobType.normal) {
			final Collection<IUser> lhs = new LinkedHashSet<IUser>();
			for (final IJobMember jobMember : OrgUtils.jmm().members(job)) {
				final Object id = jobMember.getMemberId();
				if (jobMember.getMemberType() == EMemberType.user) {
					final IUser user = OrgUtils.um().queryForObjectById(id);
					if (user != null) {
						lhs.add(user);
					}
				} else {
					final Collection<? extends IUser> coll2 = users(
							OrgUtils.jm().queryForObjectById(id), objects);
					if (coll2 != null) {
						lhs.addAll(coll2);
					}
				}
			}
			return lhs;
		} else if (jobType == EJobType.handle) {
			final IJobRule jr = job.jobRuleHandle();
			if (jr != null) {
				return jr.members(objects);
			}
		}
		return null;
	}

	static final String JOB_ID = "__job_Id";

	public String getJobIdParameterName() {
		return JOB_ID;
	}
}
