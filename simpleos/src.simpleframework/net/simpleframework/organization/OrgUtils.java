package net.simpleframework.organization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.IQueryEntityManager;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.core.ALoggerAware;
import net.simpleframework.core.ApplicationModuleException;
import net.simpleframework.core.Logger;
import net.simpleframework.organization.account.AccountManager;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.organization.impl.DepartmentManager;
import net.simpleframework.organization.impl.JobChartManager;
import net.simpleframework.organization.impl.JobManager;
import net.simpleframework.organization.impl.JobMemberManager;
import net.simpleframework.organization.impl.UserManager;
import net.simpleframework.organization.jobrule.JrAccountLocked;
import net.simpleframework.organization.jobrule.JrAccountNormal;
import net.simpleframework.organization.jobrule.JrAccountRegister;
import net.simpleframework.organization.jobrule.JrAdmin;
import net.simpleframework.organization.jobrule.JrAnonymous;
import net.simpleframework.util.ImageUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.ui.tabs.TabHref;
import net.simpleframework.web.page.component.ui.tabs.TabsUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class OrgUtils {
	static final Logger logger = ALoggerAware.getLogger(OrgUtils.class);

	public static OrganizationApplicationModule applicationModule;

	public static String deployPath;

	public static String getCssPath(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		sb.append(deployPath).append("css/").append(applicationModule.getSkin(requestResponse));
		return sb.toString();
	}

	public static ITableEntityManager getTableEntityManager(final Class<?> beanClazz) {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule, beanClazz);
	}

	public static ITableEntityManager getTableEntityManager() {
		return DataObjectManagerUtils.getTableEntityManager(applicationModule);
	}

	public static IQueryEntityManager getQueryEntityManager() {
		return DataObjectManagerUtils.getQueryEntityManager(applicationModule.getApplication());
	}

	public static AccountManager am() {
		return applicationModule.getAccountMgr();
	}

	public static JobManager jm() {
		return applicationModule.getJobMgr();
	}

	public static JobMemberManager jmm() {
		return applicationModule.getJobMemberMgr();
	}

	public static JobChartManager jcm() {
		return applicationModule.getJobChartMgr();
	}

	public static UserManager um() {
		return applicationModule.getUserMgr();
	}

	public static DepartmentManager dm() {
		return applicationModule.getDepartmentMgr();
	}

	public static void deletePhoto(final PageRequestResponse requestResponse, final Object userId) {
		final File photoCache = new File(requestResponse.getServletContext().getRealPath(OrgUtils.deployPath + "photo-cache"));
		if (!photoCache.exists()) {
			return;
		}
		for (final File photo : photoCache.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String name) {
				return name.startsWith(userId + "_");
			}
		})) {
			photo.delete();
		}
	}

	public static IUser getUserByObject(final Object userObject) {
		if (userObject instanceof IUser) {
			return (IUser) userObject;
		} else if (userObject instanceof IAccount) {
			return ((IAccount) userObject).user();
		} else {
			return OrgUtils.um().queryForObjectById(userObject);
		}
	}

	public static String getPhotoSRC(final HttpServletRequest request, final Object userObject, final int width, final int height) {
		final StringBuilder sb = new StringBuilder();
		final IUser user = getUserByObject(userObject);
		final InputStream inputStream;
		if (user == null || (inputStream = user.getPhoto()) == null) {
			sb.append(OrgUtils.deployPath).append("images/none.gif");
		} else {
			final File photoCache = new File(request.getSession().getServletContext()
					.getRealPath(OrgUtils.deployPath + File.separator + "photo-cache"));
			if (!photoCache.exists()) {
				IoUtils.createDirectoryRecursively(photoCache);
			}
			final String filename = user.getId() + "_" + width + "_" + height + ".png";
			final File photoFile = new File(photoCache.getAbsolutePath() + File.separator + filename);
			if (!photoFile.exists() || photoFile.length() == 0) {
				try {
					ImageUtils.thumbnail(inputStream, width, height, new FileOutputStream(photoFile));
				} catch (final IOException e) {
					throw ApplicationModuleException.wrapException(e);
				}
			}
			sb.append(OrgUtils.deployPath).append("photo-cache/").append(filename);
		}
		sb.insert(0, request.getContextPath());
		return sb.toString();
	}

	public static String getPhotoSRC(final HttpServletRequest request, final Object userId) {
		return getPhotoSRC(request, userId, 128, 128);
	}

	/* 权限 */
	public static boolean isManagerMember(final IUser user, final Object... objects) {
		return isMember(IJob.sj_manager, user, objects);
	}

	public static boolean isManagerMember(final HttpSession httpSession, final Object... objects) {
		return isMember(IJob.sj_manager, httpSession, objects);
	}

	public static boolean isMember(final String jn, final HttpSession httpSession, final Object... objects) {
		final IAccount account = AccountSession.getLogin(httpSession);
		return isMember(jn, account != null ? account.user() : null);
	}

	private static Map<String, IJobRule> jrMap;
	static {
		jrMap = new LinkedHashMap<String, IJobRule>();
		jrMap.put(IJob.sj_anonymous, JrAnonymous.jr);
		jrMap.put(IJob.sj_account_normal, JrAccountNormal.jr);
		jrMap.put(IJob.sj_account_locked, JrAccountLocked.jr);
		jrMap.put(IJob.sj_account_locked, JrAccountRegister.jr);
	}

	public static boolean isMember(final String jn, final IUser user, final Object... objects) {
		if (JrAdmin.jr.isMember(user, objects)) {
			return true;
		}
		if (!StringUtils.hasText(jn)) {
			return true;
		}
		for (final Map.Entry<String, IJobRule> entry : jrMap.entrySet()) {
			if (jn.equals(entry.getKey())) {
				return entry.getValue().isMember(user, objects);
			}
		}
		for (final String _jn : StringUtils.split(jn)) {
			if (_isMember(_jn, user, objects)) {
				return true;
			}
		}
		return false;
	}

	private static boolean _isMember(final String jn, final IUser user, final Object... objects) {
		if (jn.startsWith("#")) { // 如果jn开始#则认为是用户名
			final IUser user2;
			if ((user2 = um().getUserByName(jn.substring(1))) == null) {
				return false;
			}
			if (user2.equals(user)) {
				return true;
			}
		} else {
			IJob job;
			if ((job = jm().getJobByName(jn)) == null) {
				return false;
			}
			return jm().member(user, job, objects);
		}
		return false;
	}

	public static String tabs(final PageRequestResponse requestResponse) {
		final List<TabHref> tabHrefs = new ArrayList<TabHref>();
		TabHref tabHref;

		tabHref = new TabHref("#(Itsite.menu.mgruser)", "/manager/user.html");
		tabHrefs.add(tabHref);
		tabHref = new TabHref("#(Itsite.menu.mgrjob)", "/manager/job.html");
		tabHrefs.add(tabHref);

		return TabsUtils.tabs(requestResponse, tabHrefs.toArray(new TabHref[tabHrefs.size()]));
	}
}
