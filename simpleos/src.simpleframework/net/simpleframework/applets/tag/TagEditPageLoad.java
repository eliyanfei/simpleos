package net.simpleframework.applets.tag;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class TagEditPageLoad extends DefaultPageHandle {

	public void optionLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final TagBean tag = TagUtils.getTableEntityManager().queryForObjectById(
				pageParameter.getRequestParameter(ITagApplicationModule._TAG_ID), TagBean.class);
		if (tag != null) {
			dataBinding.put("to_type", tag.getTtype());
		}
	}
}
