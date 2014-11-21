package net.simpleframework.organization.impl;

import net.simpleframework.core.bean.AbstractDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IJobMember;
import net.simpleframework.organization.OrgUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class JobMember extends AbstractDataObjectBean implements IJobMember {
	private static final long serialVersionUID = -5034981550376090323L;

	private ID jobId;

	private EMemberType memberType;

	private ID memberId;

	private boolean primaryJob;

	private String description;

	@Override
	public ID getJobId() {
		return jobId;
	}

	@Override
	public void setJobId(final ID jobId) {
		this.jobId = jobId;
	}

	@Override
	public EMemberType getMemberType() {
		return memberType;
	}

	@Override
	public void setMemberType(final EMemberType memberType) {
		this.memberType = memberType;
	}

	@Override
	public ID getMemberId() {
		return memberId;
	}

	@Override
	public void setMemberId(final ID memberId) {
		this.memberId = memberId;
	}

	@Override
	public boolean isPrimaryJob() {
		return primaryJob;
	}

	@Override
	public void setPrimaryJob(final boolean primaryJob) {
		this.primaryJob = primaryJob;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public IIdBeanAware memberBean() {
		return memberBean(getMemberType(), getMemberId());
	}

	public static IIdBeanAware memberBean(final EMemberType memberType, final Object memberId) {
		return (memberType == EMemberType.user ? OrgUtils.um() : OrgUtils.jm())
				.queryForObjectById(memberId);
	}
}
