package net.simpleframework.content.component.filepager;

import java.util.HashMap;
import java.util.Map;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileUploadHandle extends AbstractSwfUploadHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		final ComponentParameter nComponentParameter = FilePagerUtils.getComponentParameter(compParameter);
		if (nComponentParameter.componentBean != null) {
			if ("selector".equals(beanProperty)) {
				return nComponentParameter.getBeanProperty("selector") + ",#swfForm";
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

	@Override
	public void upload(final ComponentParameter compParameter, final IMultipartFile multipartFile, final HashMap<String, Object> json) {
		final ComponentParameter nComponentParameter = FilePagerUtils.getComponentParameter(compParameter);
		final Map<String, Object> data = new HashMap<String, Object>();
		data.put("multipartFile", multipartFile);
		final IFilePagerHandle fHandle = (IFilePagerHandle) nComponentParameter.getComponentHandle();
		fHandle.doAdd(nComponentParameter, data);
		final String jsCallback = fHandle.getJavascriptCallback(nComponentParameter, "upload", null);
		if (StringUtils.hasText(jsCallback)) {
			json.put("jsCallback", jsCallback);
		}
	}
}
