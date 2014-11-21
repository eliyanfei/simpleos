package net.simpleframework.content.component.filepager;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.web.page.component.ComponentParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileEditPageLoad extends DefaultPageHandle {
	public void filePropsLoaded(final PageParameter pageParameter,
			final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final ComponentParameter nComponentParameter = FilePagerUtils
				.getComponentParameter(pageParameter);
		final IFilePagerHandle handle = (IFilePagerHandle) nComponentParameter.getComponentHandle();
		final FileBean fileBean = handle.getEntityBeanByRequest(nComponentParameter);
		if (fileBean != null) {
			dataBinding.put("file_topic", fileBean.getTopic());
			dataBinding.put("file_description", fileBean.getDescription());
		}
	}
}
