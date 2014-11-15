package net.simpleframework.desktop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import net.itsite.utils.IOUtils;
import net.itsite.utils.StringsUtils;
import net.itsite.utils.UUIDHexGenerator;
import net.simpleframework.organization.account.AccountSession;
import net.simpleframework.organization.account.IAccount;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;

public class ThemeUpLoadFileHandle extends AbstractSwfUploadHandle {

	@Override
	public void upload(ComponentParameter compParameter, IMultipartFile multipartFile, HashMap<String, Object> json) {
		IAccount account = AccountSession.getLogin(compParameter.getSession());
		if (account != null) {
			InputStream in = null;
			OutputStream out = null;
			try {
				String path = compParameter.request.getRealPath(compParameter.getContextPath() + "/desktop/themes/theme") + "/" + account.getId();
				File file = new File(path);
				file.mkdirs();
				if (file.listFiles().length > 10) {
					json.put("js", "false");
					return;
				}
				String fn = multipartFile.getOriginalFilename();
				final int i = fn.lastIndexOf('.');
				String fileName = StringsUtils.u(path + "\\" + UUIDHexGenerator.generator() + "." + fn.substring(i, fn.length()));
				in = multipartFile.getInputStream();
				out = new FileOutputStream(fileName);
				IOUtils.copyStream(in, out);
				json.put("js", "true");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeIO(in, out);
			}
		}
	}
}
