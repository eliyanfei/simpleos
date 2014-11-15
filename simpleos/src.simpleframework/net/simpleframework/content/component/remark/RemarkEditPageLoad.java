package net.simpleframework.content.component.remark;

import java.util.List;
import java.util.Map;

import net.simpleframework.util.StringUtils;
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
public class RemarkEditPageLoad extends DefaultPageHandle {

	public void editLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final String parentId = pageParameter.getRequestParameter("parentId");
		if (StringUtils.hasText(parentId)) {
			dataBinding.put("parentId", parentId);
		}

		final String itemId = pageParameter.getRequestParameter("itemId");
		if (StringUtils.hasText(itemId)) {
			dataBinding.put("itemId", itemId);
			final ComponentParameter nComponentParameter = RemarkUtils
					.getComponentParameter(pageParameter);
			final RemarkItem item = ((IRemarkHandle) nComponentParameter.getComponentHandle())
					.getEntityBeanById(nComponentParameter, itemId);
			if (item != null) {
				dataBinding.put("textareaRemarkHtmlEditor", item.getContent());
			}
		}
	}
}
