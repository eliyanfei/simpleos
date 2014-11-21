package net.simpleos.backend.slide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.itsite.utils.IOUtils;
import net.itsite.utils.StringsUtils;
import net.itsite.utils.UID;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;
import net.simpleos.backend.BackendUtils;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 上午11:53:56 
 * @Description: 上传首页幻灯片图片
 *
 */
public class IndexSlideUploadHandle extends AbstractSwfUploadHandle {

	@Override
	public void upload(ComponentParameter compParameter, IMultipartFile multipartFile, HashMap<String, Object> json) {
		IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			InputStream in = null;
			OutputStream out = null;
			try {
				String navId = compParameter.getParameter("navId");
				IndexSlideBean navBean = BackendUtils.applicationModule.getBean(IndexSlideBean.class, navId);
				if (navBean == null) {
					return;
				}
				String path = compParameter.getApplicationAbsolutePath("/nav");
				File file = new File(path);
				file.mkdirs();
				try {
					new File(path + navBean.getImage()).delete();
				} catch (Exception e) {
				}
				String id = UID.asString();
				String fName = multipartFile.getOriginalFilename();
				String extendsion = fName.substring(fName.lastIndexOf("."), fName.length());
				String fileName = StringsUtils.u(path + id + "." + extendsion);
				in = multipartFile.getInputStream();
				out = new FileOutputStream(fileName);
				IOUtils.copyStream(in, out);
				navBean.setImage(id + "." + extendsion);
				BackendUtils.applicationModule.doUpdate(navBean);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeIO(in, out);
			}
		}
	}
}
