package net.simpleframework.my.file;

import java.util.Date;

import net.simpleframework.ado.DataObjectManagerUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.SQLValue;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class MyFileUtils {
	public static IFileApplicationModule applicationModule;

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

	static final int DELETE_ID = -2;

	static final int ALL_ID = -3;

	static String __my_files_list(final String folderIdParameterName, final Object idValue) {
		final StringBuilder sb = new StringBuilder();
		sb.append("$Actions['__my_files']('");
		sb.append(folderIdParameterName);
		sb.append("=").append(StringUtils.blank(idValue)).append("');");
		return sb.toString();
	}

	static MyFileStat getFileStat(final PageRequestResponse requestResponse) {
		final IAccount account = AccountSession.getLogin(requestResponse.getSession());
		return getFileStat(requestResponse, account.getId());
	}

	static MyFileStat getFileStat(final PageRequestResponse requestResponse, final ID userId) {
		final ITableEntityManager temgr = getTableEntityManager(MyFileStat.class);
		MyFileStat stat = temgr.queryForObjectById(userId, MyFileStat.class);
		if (stat == null) {
			stat = new MyFileStat();
			stat.setId(userId);
			temgr.insert(stat);
			createDefaultFolders(userId);
		}
		return stat;
	}

	static long getFileSizeLimit(final PageRequestResponse requestResponse, final MyFileStat stat) {
		final long fileSizeLimit = stat.getFileSizeLimit();
		return fileSizeLimit > 0 ? fileSizeLimit : applicationModule
				.getDefaultFileSizeLimit(requestResponse);
	}

	static long getFileUploadLimit(final PageRequestResponse requestResponse, final MyFileStat stat) {
		final long fileUploadLimit = stat.getFileUploadLimit();
		return fileUploadLimit > 0 ? fileUploadLimit : applicationModule
				.getDefaultFileUploadLimit(requestResponse);
	}

	static void createDefaultFolders(final ID userId) {
		final ITableEntityManager temgr = getTableEntityManager(MyFolder.class);
		if (temgr.getCount(new ExpressionValue("userid=?", new Object[] { userId })) > 0) {
			return;
		}
		final String[] defaultFolders = new String[] { "MyFileUtils.2", "MyFileUtils.3",
				"MyFileUtils.4", "MyFileUtils.5" };
		final Date createDate = new Date();
		for (final String defaultFolder : defaultFolders) {
			final MyFolder myFolder = new MyFolder();
			myFolder.setCreateDate(createDate);
			myFolder.setText(LocaleI18n.getMessage(defaultFolder));
			myFolder.setUserId(userId);
			temgr.insert(myFolder);
		}
	}

	public static String toFileInfo(final PageRequestResponse requestResponse) {
		final StringBuilder sb = new StringBuilder();
		final MyFileStat stat = getFileStat(requestResponse);
		if (stat == null) {
			return "";
		}
		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\"><tr>");
		sb.append("<td class=\"lbl\">").append(LocaleI18n.getMessage("MyFileUtils.0"))
				.append("</td>");
		sb.append("<td>");
		sb.append("<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 100%;\"><tr><td>");
		sb.append(stat.getAllFiles());
		sb.append("</td><td align=\"right\"><a class=\"a2\" ");
		sb.append("onclick=\"$Actions['fileOptionsWindow']();\">")
				.append(LocaleI18n.getMessage("MyFileUtils.6")).append("</a>");
		sb.append("</td></tr></table></td></tr><tr>");
		sb.append("<td class=\"lbl\">").append(LocaleI18n.getMessage("MyFileUtils.1"))
				.append("</td>");
		sb.append("<td>");
		sb.append(IoUtils.toFileSize(stat.getAllFilesSize())).append(" / ")
				.append(IoUtils.toFileSize(getFileSizeLimit(requestResponse, stat)));
		sb.append("</td></tr></table>");
		return sb.toString();
	}

	public static MyFolder getFolderById(final ComponentParameter compParameter, final Object id) {
		final ComponentParameter nComponentParameter = ComponentParameter.get(compParameter,
				getFolderComponentBean(compParameter));
		final FolderHandle hdl = (FolderHandle) nComponentParameter.getComponentHandle();
		return hdl.getEntityBeanById(nComponentParameter, id);
	}

	static AbstractComponentBean getFolderComponentBean(final PageRequestResponse requestResponse) {
		return AbstractComponentBean.getComponentBeanByName(requestResponse, deployPath
				+ "jsp/folder_c.xml", "__my_folder");
	}

	static AbstractComponentBean getFileComponentBean(final PageRequestResponse requestResponse) {
		return AbstractComponentBean.getComponentBeanByName(requestResponse, deployPath
				+ "jsp/file_c.xml", "__my_files");
	}

	static void doStatRebuild() {
		final ITableEntityManager file_mgr = getTableEntityManager(MyFile.class);
		final ITableEntityManager folder_mgr = getTableEntityManager(MyFolder.class);
		final ITableEntityManager file_stat_mgr = getTableEntityManager(MyFileStat.class);

		file_mgr.execute(new SQLValue("update " + file_mgr.getTablename()
				+ " set catalogid=0 where catalogid<0"));
		file_mgr.reset();

		final int del = EContentStatus.delete.ordinal();
		folder_mgr.execute(updateFolderSql("files", "count", "status<>" + del));
		folder_mgr.execute(updateFolderSql("filessize", "sum", "status<>" + del));
		folder_mgr.reset();

		file_stat_mgr.execute(updateStatSql("allfiles", "count", null));
		file_stat_mgr.execute(updateStatSql("allfilessize", "sum", null));
		final String con = "status<>" + del + " and catalogid=0";
		file_stat_mgr.execute(updateStatSql("rootfiles", "count", con));
		file_stat_mgr.execute(updateStatSql("rootfilessize", "sum", con));
		file_stat_mgr.execute(updateStatSql("deletefiles", "count", "status=" + del));
		file_stat_mgr.execute(updateStatSql("deletefilessize", "sum", "status=" + del));
		file_stat_mgr.reset();
	}

	private static SQLValue updateFolderSql(final String column, final String func, final String con) {
		final StringBuilder sql = new StringBuilder();
		final String file_name = getTableEntityManager(MyFile.class).getTablename();
		final String folder_name = getTableEntityManager(MyFolder.class).getTablename();
		sql.append("update ").append(folder_name).append(" set ");
		sql.append(column).append("=(select ").append(func).append("(filesize) from ");
		sql.append(file_name).append(" where catalogid=").append(folder_name).append(".id");
		if (StringUtils.hasText(con)) {
			sql.append(" and ").append(con);
		}
		sql.append(")");
		return new SQLValue(sql.toString());
	}

	private static SQLValue updateStatSql(final String column, final String func, final String con) {
		final StringBuilder sql = new StringBuilder();
		final String file_name = getTableEntityManager(MyFile.class).getTablename();
		final String file_stat_name = getTableEntityManager(MyFileStat.class).getTablename();
		sql.append("update ").append(file_stat_name).append(" set ");
		sql.append(column).append("=(select ").append(func).append("(filesize) from ");
		sql.append(file_name).append(" where userid=").append(file_stat_name).append(".id");
		if (StringUtils.hasText(con)) {
			sql.append(" and ").append(con);
		}
		sql.append(")");
		return new SQLValue(sql.toString());
	}
}
