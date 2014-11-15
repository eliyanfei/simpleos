package net.simpleframework.content.component.remark;

import java.util.List;
import java.util.Map;

import net.itsite.document.docu.DocuRemark;
import net.itsite.document.docu.DocuUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

public class RemarkDocuEditPageLoad extends DefaultPageHandle {

	public void editLoaded(final PageParameter pageParameter, final Map<String, Object> dataBinding, final List<String> visibleToggleSelector,
			final List<String> readonlySelector, final List<String> disabledSelector) {
		final String parentId = pageParameter.getRequestParameter("parentId");
		if (StringUtils.hasText(parentId)) {
			dataBinding.put("parentId", parentId);
		}

		final String itemId = pageParameter.getRequestParameter("itemId");
		if (StringUtils.hasText(itemId)) {
			dataBinding.put("itemId", itemId);
			final RemarkItem item = DocuUtils.applicationModule.getBean(DocuRemark.class, itemId);
			if (item != null) {
				dataBinding.put("textareaRemarkHtmlEditor", item.getContent());
			}
		}
	}
}
