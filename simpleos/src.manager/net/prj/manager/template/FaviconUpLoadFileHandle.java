package net.prj.manager.template;

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
public class FaviconUpLoadFileHandle extends AbstractSwfUploadHandle {

	@Override
	public void upload(ComponentParameter compParameter, IMultipartFile multipartFile, HashMap<String, Object> json) {
		IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			InputStream in = null;
			OutputStream out = null;
			try {
				String path = compParameter.request.getRealPath("/simple/main/image");
				File file = new File(path);
				file.mkdirs();
				String fileName = StringsUtils.u(path + "\\favicon.png");
				in = multipartFile.getInputStream();
				out = new FileOutputStream(fileName);
				IOUtils.copyStream(in, out);
				json.put("rs", "true");
				json.put("src", compParameter.getContextPath() + "/simple/main/image/favicon.png?v=" + UID.asString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeIO(in, out);
			}
		}
	}
}
