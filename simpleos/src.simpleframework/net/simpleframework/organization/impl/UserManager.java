package net.simpleframework.organization.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.organization.AbstractOrganizationManager;
import net.simpleframework.organization.EJobType;
import net.simpleframework.organization.EMemberType;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IJobMember;
import net.simpleframework.organization.IJobRule;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.BeanUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.util.db.DbUtils;
import net.simpleframework.web.page.component.HandleException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class UserManager extends AbstractOrganizationManager<IUser> {
	public UserManager() {
		addListener(new TableEntityAdapter() {
			@Override
			public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
				OrgUtils.um().deleteLob(dataObjectValue);
				OrgUtils.am().delete(dataObjectValue);

				// 删除成员
				final Object[] values = dataObjectValue.getValues();
				final ArrayList<Object> al = new ArrayList<Object>(Arrays.asList(values));
				al.add(0, EMemberType.user);
				final ExpressionValue ev2 = new ExpressionValue("membertype=? and " + DbUtils.getIdsSQLParam("memberid", values.length), al.toArray());
				OrgUtils.jmm().delete(ev2);
			}
		});
	}

	@Override
	public Class<? extends IUser> getBeanClass() {
		return User.class;
	}

	public IUser getUserByName(final String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		return queryForObject(new ExpressionValue("name=?", new Object[] { name }));
	}

	public IUser getUserByEmail(final String email) {
		if (!StringUtils.hasText(email)) {
			return null;
		}
		return queryForObject(new ExpressionValue("email=?", new Object[] { email }));
	}

	public IUser createUser(final Object id) {
		final IUser user = BeanUtils.newInstance(getBeanClass());
		user.setId(id(id));
		return user;
	}

	public InputStream getPhotoLob(final IUser user) {
		final UserLob lob = OrgUtils.getTableEntityManager(UserLob.class).queryForObjectById(user.getId(), UserLob.class);
		return lob != null ? lob.getPhoto() : null;
	}

	public void deleteLob(final IDataObjectValue dataObjectValue) {
		OrgUtils.getTableEntityManager(UserLob.class).delete(dataObjectValue);
	}

	public void updateLob(final IUser user, final InputStream photo) {
		final ITableEntityManager lobmgr = OrgUtils.getTableEntityManager(UserLob.class);
		UserLob lob = lobmgr.queryForObjectById(user.getId(), UserLob.class);
		if (lob == null) {
			lob = new UserLob();
			lob.setId(user.getId());
			lob.setPhoto(photo);
			lobmgr.insert(lob);
		} else {
			lob.setPhoto(photo);
			lobmgr.update(lob);
		}
	}

	public Collection<IJob> jobs(final IUser user, final boolean rule) {
		final Collection<IJob> coll = new ArrayList<IJob>();
		if (user == null) {
			return coll;
		}
		final JobManager jm = OrgUtils.jm();
		final IQueryEntitySet<IJobMember> qs = OrgUtils.jmm().query(
				new ExpressionValue("membertype=? and memberid=?", new Object[] { EMemberType.user, user.getId() }));
		IJobMember jobMember;
		while ((jobMember = qs.next()) != null) {
			final IJob job = jm.queryForObjectById(jobMember.getJobId());
			if (job != null) {
				coll.add(job);
			}
		}

		if (rule) {
			final IQueryEntitySet<IJob> jobs = OrgUtils.jm().query(
					new ExpressionValue("jobtype=? or jobtype=?", new Object[] { EJobType.handle, EJobType.script }));
			IJob job;
			while ((job = jobs.next()) != null) {
				if (job.getJobType() == EJobType.handle) {
					final IJobRule jobRule = job.jobRuleHandle();
					if (jobRule != null && jobRule.isMember(user)) {
						coll.add(job);
					}
				} else {
				}
			}
		}
		return coll;
	}

	public IJob primary(final IUser user) {
		if (user == null) {
			return null;
		}
		final IJobMember jm = OrgUtils.jmm().queryForObject(
				new ExpressionValue("membertype=? and memberid=? and primaryjob=?", new Object[] { EMemberType.user, user.getId(), Boolean.TRUE }));
		if (jm != null) {
			return OrgUtils.jm().queryForObjectById(jm.getJobId());
		}
		return OrgUtils.jm().getJobByName(IJob.sj_account_normal);
	}

	public static final String USER_ID = "userid";

	public String getUserIdParameterName() {
		return USER_ID;
	}

	static final String USER_TEXT = "__user_Text";

	public String getUserTextParameterName() {
		return USER_TEXT;
	}

	public String[] getUeTabs() {
		return null;
	}
}
