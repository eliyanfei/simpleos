package net.simpleframework.content.component.filepager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.AbstractContentPagerHandle;
import net.simpleframework.content.EContentStatus;
import net.simpleframework.core.ado.db.Table;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.bean.IIdBeanAware;
import net.simpleframework.core.id.ID;
import net.simpleframework.core.id.NullID;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.util.HTMLUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.IoUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.FilePathWrapper;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.PageRequestResponse;
import net.simpleframework.web.page.PageUtils;
import net.simpleframework.web.page.component.AbstractComponentBean;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.base.validation.ValidatorBean;
import net.simpleframework.web.page.component.ui.pager.AbstractTablePagerData;
import net.simpleframework.web.page.component.ui.pager.TablePagerColumn;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class DefaultFilePagerHandle extends AbstractContentPagerHandle implements IFilePagerHandle {

	@Override
	public void putTables(final Map<Class<?>, Table> tables) {
		tables.put(FileBean.class, FilePagerUtils.table_file);
		tables.put(FileLobBean.class, FilePagerUtils.table_file_lob);
	}

	private static TableEntityAdapter teAdapter;

	@Override
	public void handleCreated(final PageRequestResponse requestResponse, final AbstractComponentBean componentBean) {
		if (FileBean.class.equals(getEntityBeanClass())) {
			PageUtils.doDatabase(FileBean.class, componentBean);
		}

		if (teAdapter == null) {
			final ITableEntityManager temgr = getTableEntityManager(null, FileBean.class);
			teAdapter = new TableEntityAdapter() {
				@Override
				public void afterDelete(final ITableEntityManager manager, final IDataObjectValue dataObjectValue) {
					getTableEntityManager(null, FileLobBean.class).delete(dataObjectValue);
				}
			};
			temgr.addListener(teAdapter);
		}
	}

	@Override
	protected String getBeanIdName() {
		return FilePagerUtils.BEAN_ID;
	}

	@Override
	public Class<? extends IIdBeanAware> getEntityBeanClass() {
		return FileBean.class;
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeEdit(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		super.doBeforeEdit(compParameter, temgr, t, data, beanClazz);
		final FileBean fileBean = (FileBean) t;
		if (data.get("top") != null) {
			fileBean.setTtop(!fileBean.isTtop());
		} else {
			fileBean.setTopic((String) data.get("topic"));
			fileBean.setDescription((String) data.get("description"));
		}
	}

	@Override
	public <T extends IDataObjectBean> void doBeforeAdd(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		final ID catalogId = ID.Utils.newID(getCatalogId(compParameter));
		if (catalogId == null || catalogId instanceof NullID) {
			throwCatalogNull(compParameter);
		}

		final IMultipartFile multipartFile = (IMultipartFile) data.get("multipartFile");
		assertFile(compParameter, multipartFile);

		final FileBean fileBean = (FileBean) t;
		fileBean.initThis(compParameter);
		fileBean.setCatalogId(catalogId);

		final String filename = multipartFile.getOriginalFilename();
		fileBean.setFilename(filename);
		final String filetype = StringUtils.getFilenameExtension(filename);
		fileBean.setFiletype(filetype);
		fileBean.setFilesize(multipartFile.getSize());
		fileBean.setIp(HTTPUtils.getRemoteAddr(compParameter.request));
	}

	protected void assertFile(final ComponentParameter compParameter, final IMultipartFile multipartFile) {
	}

	@Override
	public <T extends IDataObjectBean> void doAddCallback(final ComponentParameter compParameter, final ITableEntityManager temgr, final T t,
			final Map<String, Object> data, final Class<T> beanClazz) {
		final FileBean fileBean = (FileBean) t;

		final FileLobBean lobBean = new FileLobBean();
		lobBean.setId(fileBean.getId());

		final IMultipartFile multipartFile = (IMultipartFile) data.get("multipartFile");
		try {
			lobBean.setLob(multipartFile.getInputStream());
		} catch (final IOException e) {
			throw HandleException.wrapException(e);
		}
		getTableEntityManager(compParameter, FileLobBean.class).insert(lobBean);
	}

	@Override
	public InputStream getFileInputStream(final ComponentParameter compParameter, final FileBean fileBean) {
		if (fileBean == null) {
			return null;
		}
		final FileLobBean lobBean = getTableEntityManager(compParameter, FileLobBean.class).queryForObjectById(fileBean.getId(), FileLobBean.class);
		return lobBean != null ? lobBean.getLob() : null;
	}

	@Override
	public String getDownloadPath(final ComponentParameter compParameter, final FileBean fileBean) {
		final StringBuilder sb = new StringBuilder();
		sb.append(FilePagerUtils.getHomePath()).append("/jsp/dl.jsp?");
		sb.append(getBeanIdName()).append("=").append(compParameter.componentBean.hashId());
		sb.append("&").append(getIdParameterName(compParameter));
		sb.append("=").append(fileBean.getId());
		return sb.toString();
	}

	@Override
	public AbstractTablePagerData createTablePagerData(final ComponentParameter compParameter) {
		return new FileTablePagerData(compParameter);
	}

	protected class FileTablePagerData extends AbstractTablePagerData {
		public FileTablePagerData(final ComponentParameter compParameter) {
			super(compParameter);
		}

		@Override
		protected Map<Object, Object> getRowAttributes(final Object dataObject) {
			final Map<Object, Object> attributes = super.getRowAttributes(dataObject);
			final FileBean file = (FileBean) dataObject;
			attributes.put("top", file.isTtop());
			return attributes;
		}

		@Override
		protected Map<Object, Object> getRowData(final Object dataObject) {
			final FilePagerBean filePager = (FilePagerBean) compParameter.componentBean;
			final FileBean file = (FileBean) dataObject;
			final Map<Object, Object> rowData = new HashMap<Object, Object>();
			rowData.put("icon", FilePagerUtils.getFileImage(ComponentParameter.get(compParameter, filePager), file));
			final StringBuilder sb = new StringBuilder();
			sb.append("<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%;\"><tr><td>");
			sb.append(getTopic(file));
			sb.append("</td><td align=\"right\" width=\"20px;\">");
			if (file.isTtop()) {
				sb.append("<span class=\"image_top\"></span>");
			}
			sb.append("</td></tr>");
			final String desc = file.getDescription();
			if (StringUtils.hasText(desc)) {
				sb.append("<tr><td colspan=\"2\" style=\"padding: 2px 0;\" class=\"gray-color\">");
				sb.append(HTMLUtils.convertHtmlLines(desc));
				sb.append("</td></tr>");
			}
			sb.append("</table>");
			rowData.put("filename", sb.toString());
			rowData.put("filesize", IoUtils.toFileSize(file.getFilesize()));
			rowData.put("createdate", ConvertUtils.toDateString(file.getCreateDate()));
			rowData.put("downloads", String.valueOf(file.getDownloads()));
			rowData.put("action", ACTIONc);
			return rowData;
		}

		protected String getTopic(final FileBean file) {
			final StringBuilder sb = new StringBuilder();
			if (file.getStatus() != EContentStatus.delete) {
				sb.append("<a style=\"vertical-align: middle;\" onclick=\"");
				sb.append("__pager_action(this).download('").append(file.getId()).append("');");
				sb.append("\">");
			}
			sb.append(StringUtils.text(file.getTopic(), file.getFilename()));
			if (file.getStatus() != EContentStatus.delete) {
				sb.append("</a>");
			}
			return sb.toString();
		}
	}

	static final String FILE_ID = "__file_Id";

	@Override
	public String getIdParameterName(final ComponentParameter compParameter) {
		return FILE_ID;
	}

	@Override
	public void doDownload(final ComponentParameter compParameter, final FileBean fileBean) {
		if (fileBean == null) {
			return;
		}
		final FilePathWrapper fp = getFileCache(compParameter);
		final String filename = fileBean.getId() + "."
				+ StringUtils.text(StringUtils.getFilenameExtension(fileBean.getFilename()), fileBean.getFiletype());
		final File downloadFile = new File(fp.getFile().getAbsolutePath() + File.separator + filename);
		try {
			if (!downloadFile.exists() || downloadFile.length() == 0) {
				IoUtils.copyFile(getFileInputStream(compParameter, fileBean), downloadFile);
			}
			if (ConvertUtils.toBoolean(compParameter.getRequestParameter("loc"), false)) {
				compParameter.loc(fp.getPath() + filename);
			} else {
				if (!downloadFile.exists() || downloadFile.length() == 0) {
					throw new FileNotFoundException();
				}
				IoUtils.copyStream(new FileInputStream(downloadFile),
						compParameter.getFileOutputStream(fileBean.getFilename(), fileBean.getFilesize()));
				fileBean.setDownloads(fileBean.getDownloads() + 1);
				getTableEntityManager(compParameter, FileBean.class).update(fileBean);
			}
		} catch (final IOException e) {
			throw HandleException.wrapException(e);
		}
	}

	@Override
	protected Object convert(final TablePagerColumn oCol, final String val) {
		if ("filesize".equals(oCol.getColumnName())) {
			return IoUtils.toFileSize(val);
		} else {
			final Object o = super.convert(oCol, val);
			return o;
		}
	}

	@Override
	public Collection<ValidatorBean> getFilterColumnValidators(final ComponentParameter compParameter, final TablePagerColumn oCol) {
		return null;
	}
}
