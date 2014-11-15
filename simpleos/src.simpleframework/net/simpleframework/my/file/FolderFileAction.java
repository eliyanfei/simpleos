package net.simpleframework.my.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.OrgUtils;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IForward;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.base.ajaxrequest.AbstractAjaxRequestHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FolderFileAction extends AbstractAjaxRequestHandle {

	public IForward fileResize(final ComponentParameter compParameter) {

		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final IAccount login = AccountSession.getLogin(compParameter.getSession());
				if (login == null) {
					return;
				}
				final int jf = ConvertUtils.toInt(compParameter.getRequestParameter("jf"), 0);
				if (jf <= 0) {
					return;
				}
				final int points = login.getPoints();
				if (jf > points) {
					json.put("err", LocaleI18n.getMessage("FolderFileAction.0", points));
				} else {
					final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
					final long size = jf * 1024 * 1024;
					final long maxSize = 512 * 1024 * 1024;
					final long fs = MyFileUtils.getFileSizeLimit(compParameter, stat);
					if (fs + size > maxSize) {
						json.put("err", LocaleI18n.getMessage("FolderFileAction.1"));
					} else {
						final ITableEntityManager stat_mgr = MyFileUtils.getTableEntityManager(MyFileStat.class);
						stat.setFileSizeLimit(fs + size);
						stat_mgr.updateTransaction(stat, new TableEntityAdapter() {
							@Override
							public void afterUpdate(final ITableEntityManager manager, final Object[] objects) {
								login.setPoints(points - jf);
								OrgUtils.am().update(login);
							}
						});
					}
				}
			}
		});
	}

	public IForward folderRefresh(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
				json.put("myFilesSize", stat != null ? stat.getAllFilesSize() : 0);
				json.put("title", MyFileUtils.toFileInfo(compParameter));
			}
		});
	}

	public IForward fileDrop(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				final String[] drags = StringUtils.split(compParameter.getRequestParameter("drag"), ";");
				final List<MyFile> files = doDrop(compParameter, drags, compParameter.getRequestParameter("drop"));
				json.put("ok", files.size() > 0);
			}
		});
	}

	static List<MyFile> doDrop(final ComponentParameter compParameter, final Object[] drags, final Object drop) {
		final ArrayList<MyFile> files = new ArrayList<MyFile>();
		final HashMap<ID, MyFolder> folders = new HashMap<ID, MyFolder>();
		if (drags != null && drags.length > 0) {
			final ITableEntityManager folder_mgr = MyFileUtils.getTableEntityManager(MyFolder.class);
			final ITableEntityManager file_mgr = MyFileUtils.getTableEntityManager(MyFile.class);
			final ITableEntityManager stat_mgr = MyFileUtils.getTableEntityManager(MyFileStat.class);

			final int iDrop = ConvertUtils.toInt(drop, 0);
			final MyFolder dropFolder = folder_mgr.queryForObjectById(iDrop, MyFolder.class);
			long allFilesize = 0;
			for (final Object drag : drags) {
				final MyFile file;
				if (drag instanceof MyFile) {
					file = (MyFile) drag;
				} else if (drag instanceof FileBean) {
					file = file_mgr.queryForObjectById(((FileBean) drag).getId(), MyFile.class);
				} else {
					file = file_mgr.queryForObjectById(drag, MyFile.class);
				}
				final EContentStatus status = file.getStatus();
				if (file != null) {
					MyFolder dragFolder = null;
					if (status != EContentStatus.delete) {
						final ID folderId = file.getCatalogId();
						dragFolder = folders.get(folderId);
						if (dragFolder == null) {
							dragFolder = folder_mgr.queryForObjectById(folderId, MyFolder.class);
							if (dragFolder != null) {
								folders.put(folderId, dragFolder);
							}
						}
					}

					final long filesize = file.getFilesize();
					if (dragFolder != null) {
						dragFolder.setFiles(Math.max(dragFolder.getFiles() - 1, 0));
						dragFolder.setFilesSize(Math.max(dragFolder.getFilesSize() - filesize, 0));
					} else {
						final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
						if (status == EContentStatus.delete) {
							stat.setDeleteFiles(Math.max(stat.getDeleteFiles() - 1, 0));
							stat.setDeleteFilesSize(Math.max(stat.getDeleteFilesSize() - filesize, 0));
						} else {
							stat.setRootFiles(Math.max(stat.getRootFiles() - 1, 0));
							stat.setRootFilesSize(Math.max(stat.getRootFilesSize() - filesize, 0));
						}
						stat_mgr.update(stat);
					}
					allFilesize += filesize;
					if (iDrop == MyFileUtils.DELETE_ID) {
						file.setStatus(EContentStatus.delete);
					} else {
						if (status == EContentStatus.delete) {
							file.setStatus(EContentStatus.edit);
						}
						file.setCatalogId(dropFolder != null ? dropFolder.getId() : ID.zero);
					}
					files.add(file);
				}
			}
			if (dropFolder != null) {
				dropFolder.setFiles(dropFolder.getFiles() + files.size());
				dropFolder.setFilesSize(dropFolder.getFilesSize() + allFilesize);
				folders.put(dropFolder.getId(), dropFolder);
			} else {
				final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
				if (iDrop == MyFileUtils.DELETE_ID) {
					stat.setDeleteFiles(stat.getDeleteFiles() + files.size());
					stat.setDeleteFilesSize(stat.getDeleteFilesSize() + allFilesize);
				} else {
					stat.setRootFiles(stat.getRootFiles() + files.size());
					stat.setRootFilesSize(stat.getRootFilesSize() + allFilesize);
				}
				stat_mgr.update(stat);
			}
			file_mgr.update(files.toArray());
			folder_mgr.update(folders.values().toArray());
		}
		return files;
	}

	public IForward statRebuild(final ComponentParameter compParameter) {
		return jsonForward(compParameter, new JsonCallback() {
			@Override
			public void doAction(final Map<String, Object> json) {
				MyFileUtils.doStatRebuild();
			}
		});
	}
}
