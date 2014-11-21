package net.simpleframework.my.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.component.filepager.DefaultFilePagerHandle;
import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.core.ado.IDataObjectQuery;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.LongID;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLBuilder;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.menu.MenuBean;
import net.simpleframework.web.page.component.ui.menu.MenuItem;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractMyFilePagerHandle extends DefaultFilePagerHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("fileSizeLimit".equals(beanProperty)) {
			return IoUtils.toFileSize(MyFileUtils.getFileUploadLimit(compParameter, MyFileUtils.getFileStat(compParameter)));
		} else if ("title".equals(beanProperty)) {
			return getPagerTitle(compParameter, "__my_files");
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return MyFile.class;
	}

	@Override
	public IFileApplicationModule getApplicationModule() {
		return MyFileUtils.applicationModule;
	}

	@Override
	protected ExpressionValue getBeansSQL(final ComponentParameter compParameter) {
		final ArrayList<Object> al = new ArrayList<Object>();
		final StringBuilder sql = new StringBuilder();
		final IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account == null) {
			sql.append("1=2");
		} else {
			boolean delete = false;
			final long catalogId = getCatalogId(compParameter);
			if (catalogId == 0) {
				sql.append("userid=? and ").append(Table.nullExpr(getTableEntityManager(compParameter).getTable(), "catalogid"));
				al.add(account.getId());
			} else if (catalogId == MyFileUtils.DELETE_ID) {
				sql.append("userid=? and status=").append(EContentStatus.delete.ordinal());
				al.add(account.getId());
				delete = true;
			} else if (OrgUtils.isManagerMember(compParameter.getSession()) && catalogId == MyFileUtils.ALL_ID) {
				sql.append("1=1");
				delete = true;
			} else {
				sql.append("catalogid=?");
				al.add(catalogId);
			}
			if (!delete) {
				sql.append(" and status<>?");
				al.add(EContentStatus.delete);
			}
		}
		return new ExpressionValue(sql.toString(), al.toArray());
	}

	protected String getPagerTitle(final ComponentParameter compParameter, final String filePagerAction) {
		final StringBuilder sb = new StringBuilder();
		final long catalogId = getCatalogId(compParameter);
		if (catalogId == 0) {
			sb.append(LocaleI18n.getMessage("FolderHandle.0"));
			final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
			if (stat != null) {
				sb.append("&nbsp;&nbsp;(");
				sb.append(IoUtils.toFileSize(stat.getRootFilesSize()));
				sb.append(")");
			}
		} else if (catalogId == MyFileUtils.DELETE_ID) {
			sb.append(LocaleI18n.getMessage("FolderHandle.1"));
			final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
			if (stat != null) {
				sb.append("&nbsp;&nbsp;(");
				sb.append(IoUtils.toFileSize(stat.getDeleteFilesSize()));
				sb.append(")");
			}
		} else if (catalogId == MyFileUtils.ALL_ID) {
			sb.append(LocaleI18n.getMessage("FolderHandle.3"));
		} else if (catalogId > 0) {
			final String catalogIdName = getCatalogIdName(compParameter);
			sb.append("<a onclick=\"$Actions['").append(filePagerAction).append("']('");
			sb.append(catalogIdName).append("=')\">");
			sb.append(LocaleI18n.getMessage("FolderHandle.0"));
			sb.append("</a>").append(HTMLBuilder.NAV);
			MyFolder myFolder = MyFileUtils.getFolderById(compParameter, catalogId);
			int i = 0;
			final StringBuilder sb2 = new StringBuilder();
			while (myFolder != null) {
				if (i++ > 0) {
					final StringBuilder sb3 = new StringBuilder();
					sb3.append("<a onclick=\"$Actions['").append(filePagerAction).append("']('");
					sb3.append(catalogIdName).append("=").append(myFolder.getId());
					sb3.append("')\">");
					sb3.append(myFolder.getText()).append("</a>").append(HTMLBuilder.NAV);
					sb2.insert(0, sb3.toString());
				} else {
					sb2.append(myFolder.getText());
					sb2.append("&nbsp;&nbsp;(");
					sb2.append(IoUtils.toFileSize(myFolder.getFilesSize()));
					sb2.append(")");
				}
				myFolder = (MyFolder) myFolder.parent();
			}
			sb.append(sb2.toString());
		}
		sb.insert(0, "<div class=\"nav0_image\" style=\"style=margin-left: 4px;\">");
		sb.append("</div>");
		return sb.toString();
	}

	@Override
	protected void assertFile(final ComponentParameter compParameter, final IMultipartFile multipartFile) {
		final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
		if (stat.getAllFilesSize() + multipartFile.getSize() > MyFileUtils.getFileSizeLimit(compParameter, stat)) {
			throw HandleException.wrapException(LocaleI18n.getMessage("FileHandle.0"));
		}
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		if (getCatalogId(compParameter) == MyFileUtils.DELETE_ID) {
			throw HandleException.wrapException(LocaleI18n.getMessage("FileHandle.1"));
		}
		final FileBean fileBean = (FileBean) t;
		if (fileBean != null) {
			fileBean.setRefId(new LongID(compParameter.getRequestParameter("refId")));
		}
		super.doBeforeAdd(compParameter, temgr, t, data, beanClazz);
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doAddCallback(compParameter, temgr, t, data, beanClazz);

		final FileBean fileBean = (FileBean) t;
		final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
		if (stat != null) {
			stat.setAllFilesSize(stat.getAllFilesSize() + fileBean.getFilesize());
			stat.setAllFiles(stat.getAllFiles() + 1);
			if (getCatalogId(compParameter) == 0) {
				stat.setRootFilesSize(stat.getRootFilesSize() + fileBean.getFilesize());
				stat.setRootFiles(stat.getRootFiles() + 1);
			}
			getTableEntityManager(compParameter, MyFileStat.class).update(stat);
		}

		final MyFolder myFolder = MyFileUtils.getFolderById(compParameter, fileBean.getCatalogId());
		if (myFolder != null) {
			myFolder.setFiles(myFolder.getFiles() + 1);
			myFolder.setFilesSize(myFolder.getFilesSize() + fileBean.getFilesize());
			getTableEntityManager(compParameter, MyFolder.class).update(myFolder);
		}
	}

	protected boolean isDelete(final ComponentParameter compParameter) {
		final long catalogId = getCatalogId(compParameter);
		return catalogId == MyFileUtils.DELETE_ID || catalogId == MyFileUtils.ALL_ID;
	}

	@Override
	public <T extends IDataObjectBean> void doDelete(final ComponentParameter compParameter, final IDataObjectValue ev, final Class<T> beanClazz) {
		if (FileBean.class.isAssignableFrom(beanClazz)) {
			final ITableEntityManager temgr = getTableEntityManager(compParameter, beanClazz);
			final List<FileBean> deletes = IDataObjectQuery.Utils.toList(temgr.query(ev, FileBean.class));
			if (isDelete(compParameter)) {
				ev.setAttribute("deletes", deletes);
				super.doDelete(compParameter, ev, beanClazz);
			} else {
				FolderFileAction.doDrop(compParameter, deletes.toArray(), MyFileUtils.DELETE_ID);
			}
		} else {
			super.doDelete(compParameter, ev, beanClazz);
		}
	}

	@Override
	public <T extends IDataObjectBean> void doDeleteCallback(final ComponentParameter compParameter, final IDataObjectValue dataObjectValue,
			final Class<T> beanClazz) {
		super.doDeleteCallback(compParameter, dataObjectValue, beanClazz);
		final List<?> deletes = (List<?>) dataObjectValue.getAttribute("deletes");
		final Map<ID, MyFolder> folders = new HashMap<ID, MyFolder>();
		final Map<ID, MyFileStat> stats = new HashMap<ID, MyFileStat>();
		for (final Object delete : deletes) {
			final FileBean file = (FileBean) delete;
			final long filesize = file.getFilesize();
			final ID userId = file.getUserId();
			MyFileStat stat = stats.get(userId);
			if (stat == null) {
				stats.put(userId, stat = MyFileUtils.getFileStat(compParameter, file.getUserId()));
			}
			if (file.getStatus() == EContentStatus.delete) {
				stat.setDeleteFiles(stat.getDeleteFiles() - 1);
				stat.setDeleteFilesSize(stat.getDeleteFilesSize() - filesize);
			} else {
				final ID folderId = file.getCatalogId();
				MyFolder myFolder = folders.get(folderId);
				if (myFolder == null) {
					folders.put(folderId, myFolder = MyFileUtils.getFolderById(compParameter, folderId));
				}
				if (myFolder != null) {
					myFolder.setFiles(Math.max(myFolder.getFiles() - 1, 0));
					myFolder.setFilesSize(Math.max(myFolder.getFilesSize() - filesize, 0));
				} else {
					stat.setRootFiles(Math.max(stat.getRootFiles() - 1, 0));
					stat.setRootFilesSize(Math.max(stat.getRootFilesSize() - filesize, 0));
				}
			}
			stat.setAllFiles(stat.getAllFiles() - 1);
			stat.setAllFilesSize(stat.getAllFilesSize() - filesize);
		}
		getTableEntityManager(compParameter, MyFolder.class).update(folders.values().toArray());
		getTableEntityManager(compParameter, MyFileStat.class).update(stats.values().toArray());
	}

	@Override
	public List<MenuItem> getContextMenu(final ComponentParameter compParameter, final MenuBean menuBean) {
		final List<MenuItem> oItems = super.getContextMenu(compParameter, menuBean);
		if (getCatalogId(compParameter) == MyFileUtils.DELETE_ID) {
			final List<MenuItem> items = new ArrayList<MenuItem>();
			items.add(oItems.get(2));
			return items;
		}
		return oItems;
	}

	@Override
	public Long getCatalogId(final ComponentParameter compParameter) {
		return ConvertUtils.toLong(super.getCatalogId(compParameter), 0);
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new FileTablePagerData(compParameter) {
			private final long catalogId = getCatalogId(compParameter);

			@Override
			protected String getTopic(final FileBean file) {
				final StringBuilder sb = new StringBuilder();
				if (catalogId != MyFileUtils.ALL_ID) {
					sb.append("<span class=\"drag_image\"></span>");
				}
				if (catalogId == MyFileUtils.ALL_ID) {
					sb.append("<span class=\"mark1\">[");
					sb.append(file.getUserText()).append("]</span>&nbsp;");
				} else if (file.getStatus() == EContentStatus.delete) {
					final MyFolder folder = MyFileUtils.getFolderById(compParameter, file.getCatalogId());
					if (folder != null) {
						sb.append("<span class=\"mark1\">[");
						sb.append(folder.getText()).append("]</span>&nbsp;");
					}
				}
				sb.append(super.getTopic(file));
				return sb.toString();
			}
		};
	}

	@Override
	public List<MenuItem> getHeaderMenu(final ComponentParameter compParameter, final MenuBean menuBean) {
		return doManagerHeaderMenu(compParameter, menuBean, this, "fileManagerToolsWindow");
	}
}
