package net.simpleframework.workflow.web.component.modellist;

import java.util.List;
import java.util.Map;

import net.simpleframework.web.page.DefaultPageHandle;
import net.simpleframework.web.page.PageParameter;
import net.simpleframework.workflow.ProcessModelBean;
import net.simpleframework.workflow.WorkflowUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ModelListPageLoad extends DefaultPageHandle {

	public void optLoad(final PageParameter pageParameter, final Map<String, Object> dataBinding,
			final List<String> visibleToggleSelector, final List<String> readonlySelector,
			final List<String> disabledSelector) {
		final ProcessModelBean processModel = WorkflowUtils.pmm().queryForObjectById(
				pageParameter.getRequestParameter("modelId"));
		if (processModel != null) {
			dataBinding.put("model_status", processModel.getStatus().ordinal());
		}
	}
}
