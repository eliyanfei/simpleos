package net.itsite.upload;

import java.io.IOException;
import java.util.HashMap;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.content.component.filepager.FileBean;
import net.simpleframework.content.component.filepager.FileLobBean;
import net.simpleframework.content.component.filepager.FilePagerUtils;
import net.simpleframework.core.id.LongID;
import net.simpleframework.my.file.MyFile;
import net.simpleframework.my.file.MyFileUtils;
import net.simpleframework.my.file.component.fileselect.FileSelectUtils;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.WebUtils;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.HandleException;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;

public class FileUploadHandle extends AbstractSwfUploadHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		final ComponentParameter nComponentParameter = FilePagerUtils.getComponentParameter(compParameter);
		if (nComponentParameter.componentBean != null) {
			if ("selector".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("selector");
			} else if ("jobUpload".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("jobAdd");
			} else if ("fileQueueLimit".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("fileQueueLimit");
			} else if ("fileSizeLimit".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("fileSizeLimit");
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	public String getDownloadPath(final ComponentParameter compParameter, final FileBean fileBean) {
		final StringBuilder sb = new StringBuilder();
		sb.append(FileSelectUtils.getHomePath()).append("/jsp/dl.jsp?");
		sb.append("__file_Id=").append(fileBean.getId());
		return sb.toString();
	}

	@Override
	public void upload(final ComponentParameter compParameter, final IMultipartFile multipartFile, final HashMap<String, Object> json) {
		final MyFile fileBean = new MyFile();
		fileBean.setCatalogId(new LongID(0));
		final String filename = multipartFile.getOriginalFilename();
		fileBean.setFilename(filename);
		final String filetype = StringUtils.getFilenameExtension(filename);
		fileBean.setFiletype(filetype);
		fileBean.setUserId(ItSiteUtil.getLoginUser(compParameter).getId());
		fileBean.setFilesize(multipartFile.getSize());
		fileBean.setIp(HTTPUtils.getRemoteAddr(compParameter.request));
		MyFileUtils.getTableEntityManager(MyFile.class).insertTransaction(fileBean, new TableEntityAdapter() {
			@Override
			public void afterInsert(ITableEntityManager manager, Object[] objects) {
				final FileLobBean lobBean = new FileLobBean();
				lobBean.setId(fileBean.getId());
				try {
					lobBean.setLob(multipartFile.getInputStream());
					MyFileUtils.getTableEntityManager(FileLobBean.class).insert(lobBean);
					//
					String downloadUrl = getDownloadPath(compParameter, fileBean);
					final ComponentParameter nComponentParameter = FileSelectUtils.getComponentParameter(compParameter);
					if (nComponentParameter.componentBean != null) {
						final String jobDownload = (String) nComponentParameter.getBeanProperty("jobDownload");
						if (StringUtils.hasText(jobDownload)) {
							downloadUrl = WebUtils.addParameters(downloadUrl, "job=" + jobDownload);
						}
					}
					if (StringUtils.hasText(downloadUrl)) {
						final StringBuilder sb = new StringBuilder();
						sb.append("<p><a href=\"");
						sb.append(FileSelectUtils.DOWNLOAD_FLAG).append(downloadUrl).append("\">");
						sb.append(StringUtils.text(fileBean.getTopic(), fileBean.getFilename())).append("</a></p>");
						json.put("download", sb.toString());
						json.put("refId", compParameter.getRequestParameter("refId"));
						json.put("refId1", compParameter.getRequestParameter("refId1"));
						json.put("path", compParameter.request.getContextPath());
					}
				} catch (final IOException e) {
					throw HandleException.wrapException(e);
				}
			}
		});
	}
}
