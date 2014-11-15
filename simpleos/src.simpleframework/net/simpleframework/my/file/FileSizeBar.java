package net.simpleframework.my.file;

import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.ui.progressbar.AbstractProgressBarHandle;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileSizeBar extends AbstractProgressBarHandle {

	@Override
	public Object getBeanProperty(final ComponentParameter compParameter, final String beanProperty) {
		if ("maxProgressValue".equals(beanProperty)) {
			final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
			if (stat != null) {
				return MyFileUtils.getFileSizeLimit(compParameter, stat);
			}
		} else if ("step".equals(beanProperty)) {
			final MyFileStat stat = MyFileUtils.getFileStat(compParameter);
			if (stat != null) {
				return stat.getAllFilesSize();
			}
		}
		return super.getBeanProperty(compParameter, beanProperty);
	}
}
