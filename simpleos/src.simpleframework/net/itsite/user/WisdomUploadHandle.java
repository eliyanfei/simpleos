package net.itsite.user;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import net.a.ItSiteUtil;
import net.simpleframework.ado.db.ITableEntityManager;
import net.simpleframework.organization.IJob;
import net.simpleframework.util.ConvertUtils;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;

public class WisdomUploadHandle extends AbstractSwfUploadHandle {

	@Override
	public Object getBeanProperty(ComponentParameter compParameter, String beanProperty) {
		if ("jobUpload".equals(beanProperty)) {
			return IJob.sj_manager;
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public void upload(final ComponentParameter compParameter, final IMultipartFile multipartFile, final HashMap<String, Object> json) {
		try {
			// 保存文件，解压文件，转换文件
			try {
				final ITableEntityManager temgr = ItSiteUtil.getTableEntityManager(ItSiteUtil.applicationModule, WisdomBean.class);
				BufferedReader br = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
				String line = br.readLine();
				int type = 0;
				try {
					type = ConvertUtils.toInt(line.split("=")[1], 0);
				} catch (Exception e1) {
				}
				while ((line = br.readLine()) != null) {
					try {
						WisdomBean wisdomBean = new WisdomBean();
						wisdomBean.setType(type);
						wisdomBean.setContent(line);
						temgr.insert(wisdomBean);
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			WisdomUtils.initJok();
		}
	}

}
