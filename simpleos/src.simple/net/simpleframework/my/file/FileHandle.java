package net.simpleframework.my.file;

import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileHandle extends AbstractMyFilePagerHandle {
	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("selector".equals(beanProperty)) {
			return "#__my_folder form, #__my_files form";
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}

	@Override
	public String getJavascriptCallback(final ComponentParameter compParameter,
			final String jsAction, final Object bean) {
		String jsCallback = StringUtils.blank(super.getJavascriptCallback(compParameter, jsAction,
				bean));
		if ("upload".equals(jsAction) || "delete".equals(jsAction)) {
			jsCallback += "__my_folder_refresh();";
		}
		return jsCallback;
	}
}
