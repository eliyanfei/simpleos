package net.itsite.permission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import net.itsite.ItSiteUtil;
import net.prj.manager.PrjMgrUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.IQueryEntitySet;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IJob;
import net.simpleframework.organization.IUser;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.impl.JobMember;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ui.menu.MenuItem;

import org.springframework.jdbc.core.JdbcTemplate;

public class PlatformUtis {
	public static Lock lock = new ReentrantLock();
	public static Map<ID, Collection<MenuItem>> DEFAULT_USER_MENUS = new HashMap<ID, Collection<MenuItem>>();
	public static DataSource dataSource;

	public static DataSource getDataSource() {
		return dataSource;
	}

	public static void setDataSource(final DataSource dataSource) {
		PlatformUtis.dataSource = dataSource;
	}

	public static final JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}

	public static boolean hasJob(final ID userId, String jobRole) {
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT J.* FROM SIMPLE_JOB_MEMBER T,SIMPLE_JOB J WHERE T.JOBID=J.ID AND T.MEMBERID=?");
		IQueryEntitySet<IJob> qsJob = OrgUtils.jm().query(new SQLValue(sql.toString(), new Object[] { userId }));
		if (qsJob != null) {
			IJob job = null;
			while ((job = qsJob.next()) != null) {
				if (jobRole.equals(job.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获得管理店面权限
	 * @return
	 */
	public static String getShopPermission(final PageRequestResponse requestResponse) {
		final IAccount account = ItSiteUtil.getLoginAccount(requestResponse);
		final StringBuffer buf = new StringBuffer();
		if (account != null) {
			if (buf.length() > 0) {
				buf.insert(0, "<span>你管理的店面是：</span><span class='brred' style='color:red;'>");
				buf.append("</span>");
			}
		}
		return buf.toString();
	}

	public static List<PermissionBean> getPermissionsByJobId(final Object jobId) {
		final List<PermissionBean> list = new ArrayList<PermissionBean>();
		if (jobId == null) {
			return list;
		}
		final IQueryEntitySet<PermissionBean> permissionBeans = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, PermissionBean.class)
				.query(new ExpressionValue(" job_id = ? ", new Object[] { jobId }), PermissionBean.class);
		PermissionBean permissionBean = null;
		while ((permissionBean = permissionBeans.next()) != null) {
			list.add(permissionBean);
		}
		return list;
	}

	public static List<PermissionBean> getPermissionsByJobIds(final Object[] jobIdArray) {
		final List<PermissionBean> list = new ArrayList<PermissionBean>();
		final StringBuffer querySql = new StringBuffer();
		querySql.append("select distinct menu_name from it_platform_permission where job_id in (");
		final StringBuffer queryValues = new StringBuffer();
		for (int i = 0; i < jobIdArray.length; i++) {
			queryValues.append(jobIdArray[i]);
			if (i != jobIdArray.length - 1) {
				queryValues.append(",");
			}
		}
		querySql.append(queryValues.toString());
		querySql.append(")");
		IQueryEntitySet<Map<String, Object>> data = PrjMgrUtils.appModule.queryBean(querySql.toString(), new Object[] {});
		Map<String, Object> dataMap = null;
		while ((dataMap = data.next()) != null) {
			final PermissionBean pb = new PermissionBean();
			pb.setMenu_name((String) dataMap.get("menu_name"));
			list.add(pb);
		}
		return list;
	}

	public static Collection<MenuItem> getPaltformMenusByUser(final IUser user, final boolean login) {
		final List<MenuItem> menuItems = new ArrayList<MenuItem>();
		try {
			lock.lock();
			if (login) {
				DEFAULT_USER_MENUS.remove(user.getId());
			}
			if (user != null) {
				final Collection<MenuItem> userMenus = DEFAULT_USER_MENUS.get(user.getId());
				if (userMenus != null && userMenus.size() > 0) {
					return userMenus;
				}
			}
			final List<MenuItem> menus = new ArrayList<MenuItem>();
			menus.addAll(ItSiteUtil.menuList);
			if (user != null) {
				// admin帐号获返回所有菜单
				if (IUser.admin.equals(user.getName()) || OrgUtils.isMember(IJob.sj_manager, user)) {
					menuItems.addAll(menus);
					DEFAULT_USER_MENUS.put(user.getId(), menuItems);
					return menuItems;
				}
			}
			// 用户没有指定角色时返回空
			final Object[] jobIdArray = PlatformUtis.getJobIds(user);
			if (jobIdArray == null || jobIdArray.length <= 0) {
				return null;
			}
			// 获取用户所属角色的所有菜单
			final List<PermissionBean> list = IDataObjectQuery.Utils.toList(ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule,
					PermissionBean.class).query(new ExpressionValue("job_id in (" + StringUtils.join(jobIdArray, ",") + ")"), PermissionBean.class));
			for (final MenuItem menu : menus) {
				if (isPermission(menu, list)) {
					menuItems.add(menu);
					doCheckedChildMenuItem(menu, list);
				}
			}
			if (user != null) {
				DEFAULT_USER_MENUS.put(user.getId(), menuItems);
			}
		} finally {
			lock.unlock();
		}
		return menuItems;
	}

	public static void doCheckedChildMenuItem(final MenuItem menu, final List<PermissionBean> list) {
		final List<MenuItem> childs = new ArrayList<MenuItem>();
		childs.addAll(menu.getChildren());
		menu.getChildren().clear();
		for (final MenuItem menuItem : childs) {
			if (isPermission(menuItem, list)) {
				menu.getChildren().add(menuItem);
				doCheckedChildMenuItem(menuItem, list);
			} else {
				// menu.getChildren().remove(menuItem);
			}
		}
	}

	public static boolean isPermission(final MenuItem menu, final List<PermissionBean> list) {
		for (final PermissionBean pb : list) {
			if (pb.getMenu_name().equals(menu.getTitle())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前登录用户的所有角色信息
	 * 
	 * @param user
	 *            登录用户
	 * @return
	 */
	public static List<JobMember> getJobMembersByUser(final IUser user) {
		final List<JobMember> jobMemberList = new ArrayList<JobMember>();
		final IQueryEntitySet<JobMember> jobMembers = OrgUtils.getTableEntityManager(JobMember.class).query(
				new ExpressionValue(" memberid = ? ", new Object[] { user.getId().getValue() }), JobMember.class);
		JobMember jobMember = null;
		while ((jobMember = jobMembers.next()) != null) {
			jobMemberList.add(jobMember);
		}
		return jobMemberList;
	}

	public static Object[] getJobIds(final IUser user) {
		final List<Object> jobIds = new ArrayList<Object>();
		if (user != null) {
			final List<JobMember> jobMembers = getJobMembersByUser(user);
			jobIds.add(OrgUtils.jm().getJobByName(IJob.sj_account_normal).getId());
			for (final JobMember jobMember : jobMembers) {
				jobIds.add(jobMember.getJobId().getValue());
			}
		} else {
			jobIds.add(OrgUtils.jm().getJobByName(IJob.sj_anonymous).getId());
		}
		return jobIds.toArray(new Object[jobIds.size()]);
	}
}
