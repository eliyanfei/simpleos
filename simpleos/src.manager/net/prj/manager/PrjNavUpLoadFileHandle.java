package net.prj.manager;

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

/**
 * 上传LOGO
 * @author yanfei.li
 * @email eliyanfei@126.com
 * 2013-12-3下午12:07:29
 */
public class PrjNavUpLoadFileHandle extends AbstractSwfUploadHandle {

	@Override
	public void upload(ComponentParameter compParameter, IMultipartFile multipartFile, HashMap<String, Object> json) {
		IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			InputStream in = null;
			OutputStream out = null;
			try {
				String navId = compParameter.getParameter("navId");
				PrjNavBean navBean = PrjMgrUtils.appModule.getBean(PrjNavBean.class, navId);
				if (navBean == null) {
					return;
				}
				String path = compParameter.request.getRealPath("/nav") + "/";
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
				PrjMgrUtils.appModule.doUpdate(navBean);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeIO(in, out);
			}
		}
	}
}
