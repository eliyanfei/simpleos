package net.simpleframework.example;

import java.util.HashMap;

import net.simpleframework.web.page.IMultipartFile;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.swfupload.AbstractSwfUploadHandle;

public class MySwfuploadHandle extends AbstractSwfUploadHandle {

	@Override
	public void upload(final ComponentParameter compParameter, final IMultipartFile multipartFile,
			final HashMap<String, Object> json) {
		System.out.println(multipartFile.getOriginalFilename());
	}
}
