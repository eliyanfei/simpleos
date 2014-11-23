package net.simpleos.backend.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;
import net.simpleos.utils.IOUtils;
import net.simpleos.utils.StringsUtils;
import net.simpleos.utils.UID;

/**
 * 
 * @author 李岩飞 
 * @email eliyanfei@126.com
 * @date 2014年11月21日 下午12:03:29 
 * @Description: 网站图片上传
 *
 */
public class FaviconUploadHandle extends AbstractSwfUploadHandle {

	@Override
	public void upload(ComponentParameter compParameter, IMultipartFile multipartFile, HashMap<String, Object> json) {
		IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			InputStream in = null;
			OutputStream out = null;
			try {
				String path = compParameter.getApplicationAbsolutePath("/images");
				File file = new File(path);
				file.mkdirs();
				String fileName = StringsUtils.u(path + "\\favicon.png");
				in = multipartFile.getInputStream();
				out = new FileOutputStream(fileName);
				IOUtils.copyStream(in, out);
				json.put("rs", "true");
				json.put("src", compParameter.getContextPath() + "/images/favicon.png?v=" + UID.asString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeIO(in, out);
			}
		}
	}
}
