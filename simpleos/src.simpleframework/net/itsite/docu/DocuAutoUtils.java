package net.itsite.docu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.itsite.ItSiteUtil;
import net.itsite.utils.IOUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.id.ID;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;

import org.apache.commons.io.FilenameUtils;

public class DocuAutoUtils {
	public static final String[] docuExts = { "doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "wps", "rtf", "xml", "txt" };
	public static final String[] videoExts = { "mp4", "flv" };
	public static final String[] rarExts = { "rar", "zip" };

	public static boolean upload(final ComponentParameter compParameter, final File multipartFile, final ID catalog) {
		try {
			final String fileName = multipartFile.getName();
			final String extension = StringUtils.getFilenameExtension(fileName);
			final ID userId = ItSiteUtil.getLoginUser(compParameter).getId();
			final String path1 = DocuUtils.getDatabase(userId);
			// 保存文件，解压文件，转换文件
			try {
				//视频默认存在d0目录下。因为加载的问题。
				//在此处需要检查磁盘

				final String key = DocuUtils.docuPath;
				if (DocuUtils.applicationModule.getDataObjectManager(DocuBean.class).getCount(
						new ExpressionValue("title=?", new Object[] { FilenameUtils.getBaseName(fileName) })) > 0) {
					return false;
				}
				// 上传文件
				IOUtils.copyStream(new FileInputStream(multipartFile), new FileOutputStream(path1 + fileName));
				final DocuBean docuBean = new DocuBean();
				docuBean.setTitle(FilenameUtils.getBaseName(fileName));
				docuBean.setExtension(extension);
				docuBean.setUserId(userId);// 上传者
				docuBean.setFileName(fileName);
				docuBean.setPath1(path1);// 存储路径
				docuBean.setPath2(key);// 存储路径
				docuBean.setStatus(EDocuStatus.edit);
				docuBean.setCatalogId(catalog);
				docuBean.setFileSize(multipartFile.length());
				if (docuBean.getFileSize() >= 1024 * 1024) {
					docuBean.setPoint(1);
				}
				docuBean.setDocuFunction(EDocuFunction.data);
				DocuUtils.applicationModule.doUpdate(docuBean, new TableEntityAdapter() {
					@Override
					public void afterInsert(ITableEntityManager manager, Object[] objects) {
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} catch (final Exception e) {
			return false;
		}
	}

}
