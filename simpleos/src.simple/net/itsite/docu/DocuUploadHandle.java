package net.itsite.docu;

import java.io.FileOutputStream;
import java.util.HashMap;

import net.itsite.ItSiteUtil;
import net.itsite.utils.IOUtils;
import net.simpleframework.ado.db.ExpressionValue;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.ado.db.event.TableEntityAdapter;
import net.simpleframework.core.id.ID;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;

import org.apache.commons.io.FilenameUtils;

public class DocuUploadHandle extends AbstractSwfUploadHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobUpload".equals(beanProperty)) {
			return IJob.sj_account_normal;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public void upload(final ComponentParameter compParameter, final IMultipartFile multipartFile, final HashMap<String, Object> json) {
		try {
			final String fileName = multipartFile.getOriginalFilename();
			final String extension = StringUtils.getFilenameExtension(fileName);
			final ID userId = ItSiteUtil.getLoginUser(compParameter).getId();
			final String path1 = DocuUtils.getDatabase(userId);
			// 保存文件，解压文件，转换文件
			try {
				if (DocuUtils.applicationModule.getDataObjectManager(DocuBean.class).getCount(
						new ExpressionValue("title=?", new Object[] { FilenameUtils.getBaseName(fileName) })) > 0) {
					json.put("id", "");
					return;
				}
				// 上传文件
				IOUtils.copyStream(multipartFile.getInputStream(), new FileOutputStream(path1 + fileName));
				final DocuBean docuBean = new DocuBean();
				docuBean.setTitle(FilenameUtils.getBaseName(fileName));
				docuBean.setExtension(extension);
				docuBean.setUserId(userId);// 上传者
				docuBean.setFileName(fileName);
				docuBean.setPath1(path1);// 存储路径
				docuBean.setPath2("");// 存储路径
				if (ItSiteUtil.isManage(compParameter)) {
					docuBean.setStatus(EDocuStatus.publish  );
				} else {
					docuBean.setStatus(EDocuStatus.audit);
				}
				docuBean.setFileSize(multipartFile.getSize());
				docuBean.setDocuFunction(EDocuFunction.data);
				DocuUtils.applicationModule.doUpdate(docuBean, new TableEntityAdapter() {
					@Override
					public void afterInsert(ITableEntityManager manager, Object[] objects) {
						json.put("id", docuBean.getId().toString());
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
