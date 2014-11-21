package net.simpleframework.organization.impl;

import java.util.ArrayList;
import java.util.Collection;

import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.AbstractOrganizationManager;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobMember;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobMemberManager extends AbstractOrganizationManager<IJobMember> {
	@Override
	public Class<? extends IJobMember> getBeanClass() {
		return JobMember.class;
	}

	public IJobMember createJobMember(final ID jobId, final EMemberType memberType, final ID memberId) {
		final JobMember jm = new JobMember();
		jm.setJobId(jobId);
		jm.setMemberType(memberType);
		jm.setMemberId(memberId);
		return jm;
	}

	public Collection<IJobMember> members(final IJob job) {
		final ArrayList<IJobMember> al = new ArrayList<IJobMember>();
		if (job != null) {
			final IDataObjectQuery<IJobMember> qs = query(new ExpressionValue("jobid=?",
					new Object[] { job.getId() }));
			IJobMember jm;
			while ((jm = qs.next()) != null) {
				al.add(jm);
			}
		}
		return al;
	}
}
